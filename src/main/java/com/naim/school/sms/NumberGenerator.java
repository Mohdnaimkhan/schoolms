package com.naim.school.sms;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.naim.school.student.Student;
import com.naim.school.student.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NumberGenerator {

    private final StudentRepository studentRepository;

    public String generateAdmissionNo() {

        Optional<Student> student =
                studentRepository.findTopByOrderByIdDesc();

        if (student.isEmpty()) {

            return "STU-00001";

        }

        return String.format(
                "STU-%05d",
                student.get().getId() + 1
        );

    }

    public String generateRollNo(Long sessionId,
                                 Long classRoomId) {

        Optional<Student> student =
                studentRepository
                        .findTopByAcademicSession_IdAndClassRoom_IdOrderByRollNumberDesc(
                                sessionId,
                                classRoomId
                        );

        if (student.isEmpty()) {

            return "001";

        }

        int next =
                Integer.parseInt(student.get().getRollNumber()) + 1;

        return String.format("%03d", next);

    }

}