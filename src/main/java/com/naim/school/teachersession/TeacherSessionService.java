package com.naim.school.teachersession;

import com.naim.school.sms.BusinessException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.section.SectionRepository;
import com.naim.school.subject.SubjectRepository;
import com.naim.school.teacher.Teacher;
import com.naim.school.teacher.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherSessionService {

    private final TeacherSessionRepo teacherSessionRepo;
    private final TeacherRepository teacherRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final SubjectRepository subjectRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;

    // Save / Update
    public TeacherSession save(TeacherSession teacherSession) {

        if (teacherSession.getTeacherId() == null) {

            throw new BusinessException("Teacher is required.");

        }

        teacherSession.setTeacher(
                teacherRepository.findById(teacherSession.getTeacherId())
                        .orElseThrow(() -> new BusinessException("Teacher not found.")));

        if (teacherSession.getAcademicSessionId() == null) {

            throw new BusinessException("Academic session is required.");

        }

        teacherSession.setAcademicSession(
                academicSessionRepository.findById(teacherSession.getAcademicSessionId())
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

        if (teacherSession.getSubjectId() == null) {

            throw new BusinessException("Subject is required.");

        }

        teacherSession.setSubject(
                subjectRepository.findById(teacherSession.getSubjectId())
                        .orElseThrow(() -> new BusinessException("Subject not found.")));

        if (teacherSession.getClassRoomId() == null) {

            throw new BusinessException("Class is required.");

        }

        teacherSession.setClassRoom(
                classRoomRepository.findById(teacherSession.getClassRoomId())
                        .orElseThrow(() -> new BusinessException("Class not found.")));

        teacherSession.setSection(
                teacherSession.getSectionId() == null
                        ? null
                        : sectionRepository.findById(teacherSession.getSectionId())
                                .orElseThrow(() -> new BusinessException("Section not found.")));

        // A teacher can validly have several different current assignments
        // (different subject/class combinations) at once, unlike a student's
        // single class placement - so the duplicate guard here is scoped to
        // the exact same teacher + subject + class + section, not the whole
        // teacher.
        if (Boolean.TRUE.equals(teacherSession.getCurrentSession())) {

            for (TeacherSession existing : teacherSessionRepo.findByCurrentSessionTrue()) {

                boolean sameAssignment = existing.getTeacher() != null
                        && existing.getTeacher().getId().equals(teacherSession.getTeacher().getId())
                        && existing.getSubject() != null
                        && existing.getSubject().getId().equals(teacherSession.getSubject().getId())
                        && existing.getClassRoom() != null
                        && existing.getClassRoom().getId().equals(teacherSession.getClassRoom().getId())
                        && !existing.getId().equals(teacherSession.getId());

                if (sameAssignment) {

                    existing.setCurrentSession(false);

                    teacherSessionRepo.save(existing);

                }

            }

        }

        return teacherSessionRepo.save(teacherSession);

    }

    // Find By Id
    public TeacherSession getById(Long id) {

        TeacherSession teacherSession = teacherSessionRepo.findById(id).orElse(null);

        if (teacherSession == null) {

            return null;

        }

        if (teacherSession.getTeacher() != null) {

            teacherSession.setTeacherId(teacherSession.getTeacher().getId());

        }

        if (teacherSession.getAcademicSession() != null) {

            teacherSession.setAcademicSessionId(teacherSession.getAcademicSession().getId());

        }

        if (teacherSession.getSubject() != null) {

            teacherSession.setSubjectId(teacherSession.getSubject().getId());

        }

        if (teacherSession.getClassRoom() != null) {

            teacherSession.setClassRoomId(teacherSession.getClassRoom().getId());

        }

        if (teacherSession.getSection() != null) {

            teacherSession.setSectionId(teacherSession.getSection().getId());

        }

        return teacherSession;

    }

    // Get All
    public List<TeacherSession> getAll() {

        return teacherSessionRepo.findAll();

    }

    // Session Wise Records
    public List<TeacherSession> getByAcademicSession(Long sessionId) {

        return teacherSessionRepo.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

    }

    // Delete
    public void delete(Long id) {

        teacherSessionRepo.deleteById(id);

    }

    // Current Assignment
    public TeacherSession getCurrentSession(Teacher teacher) {

        return teacherSessionRepo
                .findByTeacherAndCurrentSessionTrue(teacher)
                .orElse(null);

    }

    // Teacher History
    public List<TeacherSession> getTeacherHistory(Teacher teacher) {

        return teacherSessionRepo
                .findByTeacherOrderByAcademicSession_IdDesc(teacher);

    }

    // Current Assignments
    public List<TeacherSession> getCurrentAssignments() {

        return teacherSessionRepo.findByCurrentSessionTrue();

    }

    // A Teacher's Current Assignments Within One Academic Session (for Reassignment)
    public List<TeacherSession> getTeacherAssignmentsInSession(Teacher teacher, Long sessionId) {

        List<TeacherSession> result = new ArrayList<>();

        for (TeacherSession ts : teacherSessionRepo.findByTeacherOrderByAcademicSession_IdDesc(teacher)) {

            if (ts.getAcademicSession() != null && ts.getAcademicSession().getId().equals(sessionId)) {

                result.add(ts);

            }

        }

        return result;

    }

    // Reassign Selected Assignments to a New Academic Session (keeps same subject/class/section)
    public void reassignToSession(List<Long> teacherSessionIds, Long toSessionId) {

        AcademicSession toSession = academicSessionRepository.findById(toSessionId)
                .orElseThrow(() -> new BusinessException("Target academic session not found."));

        for (Long id : teacherSessionIds) {

            TeacherSession current = teacherSessionRepo.findById(id)
                    .orElseThrow(() -> new BusinessException("Teacher session not found."));

            TeacherSession newAssignment = new TeacherSession();

            newAssignment.setTeacher(current.getTeacher());
            newAssignment.setAcademicSession(toSession);
            newAssignment.setSubject(current.getSubject());
            newAssignment.setClassRoom(current.getClassRoom());
            newAssignment.setSection(current.getSection());

            current.setCurrentSession(false);

            teacherSessionRepo.save(current);

            newAssignment.setCurrentSession(true);

            teacherSessionRepo.save(newAssignment);

        }

    }

}
