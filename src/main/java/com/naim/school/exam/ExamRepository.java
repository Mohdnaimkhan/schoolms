package com.naim.school.exam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByAcademicSession(AcademicSession academicSession);

    List<Exam> findByAcademicSessionAndClassRoom(AcademicSession academicSession, ClassRoom classRoom);

}
