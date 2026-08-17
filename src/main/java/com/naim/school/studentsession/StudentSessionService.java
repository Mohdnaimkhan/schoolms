package com.naim.school.studentsession;

import com.naim.school.sms.BusinessException;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.section.Section;
import com.naim.school.section.SectionRepository;
import com.naim.school.student.Student;
import com.naim.school.student.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentSessionService {

    private final StudentSessionRepo studentSessionRepo;
    private final StudentRepository studentRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;

    // Save / Update
    public StudentSession save(StudentSession studentSession) {

        if (studentSession.getStudentId() == null) {

            throw new BusinessException("Student is required.");

        }

        studentSession.setStudent(
                studentRepository.findById(studentSession.getStudentId())
                        .orElseThrow(() -> new BusinessException("Student not found.")));

        if (studentSession.getAcademicSessionId() == null) {

            throw new BusinessException("Academic session is required.");

        }

        studentSession.setAcademicSession(
                academicSessionRepository.findById(studentSession.getAcademicSessionId())
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

        if (studentSession.getClassRoomId() == null) {

            throw new BusinessException("Class is required.");

        }

        studentSession.setClassRoom(
                classRoomRepository.findById(studentSession.getClassRoomId())
                        .orElseThrow(() -> new BusinessException("Class not found.")));

        studentSession.setSection(
                studentSession.getSectionId() == null
                        ? null
                        : sectionRepository.findById(studentSession.getSectionId())
                                .orElseThrow(() -> new BusinessException("Section not found.")));

        // Only one placement can be "current" for a student at a time -
        // if this save marks a new one as current, un-mark any other.
        if (Boolean.TRUE.equals(studentSession.getCurrentSession())) {

            studentSessionRepo.findByStudentAndCurrentSessionTrue(studentSession.getStudent())
                    .filter(existing -> !existing.getId().equals(studentSession.getId()))
                    .ifPresent(existing -> {

                        existing.setCurrentSession(false);

                        studentSessionRepo.save(existing);

                    });

        }

        return studentSessionRepo.save(studentSession);

    }

    // Find By Id
    public StudentSession getById(Long id) {

        StudentSession studentSession = studentSessionRepo.findById(id).orElse(null);

        if (studentSession == null) {

            return null;

        }

        if (studentSession.getStudent() != null) {

            studentSession.setStudentId(studentSession.getStudent().getId());

        }

        if (studentSession.getAcademicSession() != null) {

            studentSession.setAcademicSessionId(studentSession.getAcademicSession().getId());

        }

        if (studentSession.getClassRoom() != null) {

            studentSession.setClassRoomId(studentSession.getClassRoom().getId());

        }

        if (studentSession.getSection() != null) {

            studentSession.setSectionId(studentSession.getSection().getId());

        }

        return studentSession;

    }

    // Get All
    public List<StudentSession> getAll() {

        return studentSessionRepo.findAll();

    }

    // Session Wise Records
    public List<StudentSession> getByAcademicSession(Long sessionId) {

        return studentSessionRepo.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

    }

    // Delete
    public void delete(Long id) {

        studentSessionRepo.deleteById(id);

    }

    // Current Session
    public StudentSession getCurrentSession(Student student) {

        return studentSessionRepo
                .findByStudentAndCurrentSessionTrue(student)
                .orElse(null);

    }

    // Student History
    public List<StudentSession> getStudentHistory(Student student) {

        return studentSessionRepo
                .findByStudentOrderByAcademicSession_IdDesc(student);

    }

    // Current Students
    public List<StudentSession> getCurrentStudents() {

        return studentSessionRepo.findByCurrentSessionTrue();

    }

    // Session + Class Wise Students
    public List<StudentSession> getStudentsBySessionAndClass(Long sessionId,
                                                             Long classRoomId) {

        return studentSessionRepo.findByAcademicSession_IdAndClassRoom_Id(
                sessionId,
                classRoomId
        );

    }

    // Promote Student
    public StudentSession promoteStudent(StudentSession currentSession,
                                         StudentSession newSession) {

        currentSession.setCurrentSession(false);

        studentSessionRepo.save(currentSession);

        newSession.setCurrentSession(true);

        return studentSessionRepo.save(newSession);

    }

    // Bulk Promote Students to a New Session / Class / Section
    public void promoteStudents(List<Long> studentSessionIds,
                                Long toSessionId,
                                Long toClassRoomId,
                                Long toSectionId) {

        AcademicSession toSession = academicSessionRepository.findById(toSessionId)
                .orElseThrow(() -> new BusinessException("Target academic session not found."));

        ClassRoom toClassRoom = classRoomRepository.findById(toClassRoomId)
                .orElseThrow(() -> new BusinessException("Target class not found."));

        Section toSection = toSectionId == null
                ? null
                : sectionRepository.findById(toSectionId)
                        .orElseThrow(() -> new BusinessException("Target section not found."));

        for (Long id : studentSessionIds) {

            StudentSession current = studentSessionRepo.findById(id)
                    .orElseThrow(() -> new BusinessException("Student session not found."));

            StudentSession newSession = new StudentSession();

            newSession.setStudent(current.getStudent());
            newSession.setAcademicSession(toSession);
            newSession.setClassRoom(toClassRoom);
            newSession.setSection(toSection);
            newSession.setRollNumber(current.getRollNumber());
            newSession.setStatus(current.getStatus());

            promoteStudent(current, newSession);

        }

    }

}