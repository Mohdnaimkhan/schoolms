package com.naim.school.studentsession;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.student.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentSessionService {

    private final StudentSessionRepo studentSessionRepo;

    // Save / Update
    public StudentSession save(StudentSession studentSession) {

        return studentSessionRepo.save(studentSession);

    }

    // Find By Id
    public StudentSession getById(Long id) {

        return studentSessionRepo.findById(id).orElse(null);

    }

    // Get All
    public List<StudentSession> getAll() {

        return studentSessionRepo.findAll();

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
                .findByStudentOrderByAcademicSessionIdDesc(student);

    }

    // Current Students
    public List<StudentSession> getCurrentStudents() {

        return studentSessionRepo.findByCurrentSessionTrue();

    }

    // Session + Class Wise Students
    public List<StudentSession> getStudentsBySessionAndClass(Long sessionId,
                                                             Long classRoomId) {

        return studentSessionRepo.findByAcademicSessionIdAndClassRoomId(
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

}