package com.naim.school.fee;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    List<Fee> findByStudent(Student student);

    List<Fee> findByAcademicSession(AcademicSession academicSession);

    List<Fee> findByStatus(FeeStatus status);

    Optional<Fee> findByReceiptNo(String receiptNo);

    boolean existsByReceiptNo(String receiptNo);

    List<Fee> findTop5ByOrderByIdDesc();

}