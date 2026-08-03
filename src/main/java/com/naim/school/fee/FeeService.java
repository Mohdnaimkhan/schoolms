package com.naim.school.fee;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.student.Student;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FeeService {

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    public List<Fee> findAll() {
        return feeRepository.findAll();
    }

    public Optional<Fee> findById(Long id) {
        return feeRepository.findById(id);
    }

    public Fee save(Fee fee) {
        return feeRepository.save(fee);
    }

    public void deleteById(Long id) {
        feeRepository.deleteById(id);
    }

    public List<Fee> findByStudent(Student student) {
        return feeRepository.findByStudent(student);
    }

    public List<Fee> findByAcademicSession(AcademicSession academicSession) {
        return feeRepository.findByAcademicSession(academicSession);
    }

    public List<Fee> findByStatus(FeeStatus status) {
        return feeRepository.findByStatus(status);
    }

    public Optional<Fee> findByReceiptNo(String receiptNo) {
        return feeRepository.findByReceiptNo(receiptNo);
    }

    public boolean existsByReceiptNo(String receiptNo) {
        return feeRepository.existsByReceiptNo(receiptNo);
    }

    public List<Fee> findTop5ByOrderByIdDesc() {
       return feeRepository.findTop5ByOrderByIdDesc();
    }

    public Collection<Fee> findByPaymentDate(LocalDate now) {
      return feeRepository.findByPaymentDate(now);
    }

    public long countByStatus(FeeStatus pending) {
       return feeRepository.countByStatus(pending);
    }

}