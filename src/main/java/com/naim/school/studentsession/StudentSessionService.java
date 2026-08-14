package com.naim.school.studentsession;

import java.util.List;

import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.section.Section;
import com.naim.school.section.SectionRepository;
import com.naim.school.student.Student;
import com.naim.school.student.StudentRepository;
=======

import com.naim.school.student.Student;
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentSessionService {

    private final StudentSessionRepo studentSessionRepo;
<<<<<<< HEAD
    private final StudentRepository studentRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

    // Save / Update
    public StudentSession save(StudentSession studentSession) {

<<<<<<< HEAD
        if (studentSession.getStudentId() == null) {

            throw new RuntimeException("Student is required.");

        }

        studentSession.setStudent(
                studentRepository.findById(studentSession.getStudentId())
                        .orElseThrow(() -> new RuntimeException("Student not found.")));

        if (studentSession.getAcademicSessionId() == null) {

            throw new RuntimeException("Academic session is required.");

        }

        studentSession.setAcademicSession(
                academicSessionRepository.findById(studentSession.getAcademicSessionId())
                        .orElseThrow(() -> new RuntimeException("Academic session not found.")));

        if (studentSession.getClassRoomId() == null) {

            throw new RuntimeException("Class is required.");

        }

        studentSession.setClassRoom(
                classRoomRepository.findById(studentSession.getClassRoomId())
                        .orElseThrow(() -> new RuntimeException("Class not found.")));

        studentSession.setSection(
                studentSession.getSectionId() == null
                        ? null
                        : sectionRepository.findById(studentSession.getSectionId())
                                .orElseThrow(() -> new RuntimeException("Section not found.")));

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

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
        return studentSessionRepo.save(studentSession);

    }

    // Find By Id
    public StudentSession getById(Long id) {

<<<<<<< HEAD
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
=======
        return studentSessionRepo.findById(id).orElse(null);
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

    }

    // Get All
    public List<StudentSession> getAll() {

        return studentSessionRepo.findAll();

    }

<<<<<<< HEAD
    // Session Wise Records
    public List<StudentSession> getByAcademicSession(Long sessionId) {

        return studentSessionRepo.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new RuntimeException("Academic session not found.")));

    }

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
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
<<<<<<< HEAD
                .findByStudentOrderByAcademicSession_IdDesc(student);
=======
                .findByStudentOrderByAcademicSessionIdDesc(student);
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

    }

    // Current Students
    public List<StudentSession> getCurrentStudents() {

        return studentSessionRepo.findByCurrentSessionTrue();

    }

    // Session + Class Wise Students
    public List<StudentSession> getStudentsBySessionAndClass(Long sessionId,
                                                             Long classRoomId) {

<<<<<<< HEAD
        return studentSessionRepo.findByAcademicSession_IdAndClassRoom_Id(
=======
        return studentSessionRepo.findByAcademicSessionIdAndClassRoomId(
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
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

<<<<<<< HEAD
    // Bulk Promote Students to a New Session / Class / Section
    @Transactional
    public void promoteStudents(List<Long> studentSessionIds,
                                Long toSessionId,
                                Long toClassRoomId,
                                Long toSectionId) {

        AcademicSession toSession = academicSessionRepository.findById(toSessionId)
                .orElseThrow(() -> new RuntimeException("Target academic session not found."));

        ClassRoom toClassRoom = classRoomRepository.findById(toClassRoomId)
                .orElseThrow(() -> new RuntimeException("Target class not found."));

        Section toSection = toSectionId == null
                ? null
                : sectionRepository.findById(toSectionId)
                        .orElseThrow(() -> new RuntimeException("Target section not found."));

        for (Long id : studentSessionIds) {

            StudentSession current = studentSessionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student session not found."));

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

=======
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
}