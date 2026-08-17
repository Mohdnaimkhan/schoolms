package com.naim.school.fee;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.studentsession.StudentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeeRepository extends JpaRepository<Fee, Long> {

    List<Fee> findByStudentSession(StudentSession studentSession);

    List<Fee> findByStudentSession_AcademicSession(AcademicSession academicSession);

    List<Fee> findByStatus(FeeStatus status);

    Optional<Fee> findByReceiptNo(String receiptNo);

    boolean existsByReceiptNo(String receiptNo);

    List<Fee> findTop5ByOrderByIdDesc();

    List<Fee> findByPaymentDate(LocalDate paymentDate);

    long countByStatus(FeeStatus status);
}
