package com.naim.school.certificate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.student.Student;

public interface TransferCertificateRepository extends JpaRepository<TransferCertificate, Long> {

    List<TransferCertificate> findByStudentSession_Student(Student student);

    boolean existsByTcNumber(String tcNumber);

}
