package com.naim.school.fee;

import com.naim.school.sms.BusinessException;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.feehead.FeeHead;
import com.naim.school.feehead.FeeHeadRepository;
import com.naim.school.studentsession.StudentSession;
import com.naim.school.studentsession.StudentSessionRepo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FeeService {

    private final FeeRepository feeRepository;
    private final StudentSessionRepo studentSessionRepo;
    private final FeeHeadRepository feeHeadRepository;

    public FeeService(FeeRepository feeRepository,
                       StudentSessionRepo studentSessionRepo,
                       FeeHeadRepository feeHeadRepository) {
        this.feeRepository = feeRepository;
        this.studentSessionRepo = studentSessionRepo;
        this.feeHeadRepository = feeHeadRepository;
    }

    public List<Fee> findAll() {
        return feeRepository.findAll();
    }

    // Find by id, with the transient dropdown ids populated for the edit form
    public Optional<Fee> findById(Long id) {

        Optional<Fee> feeOpt = feeRepository.findById(id);

        feeOpt.ifPresent(fee -> {

            if (fee.getStudentSession() != null) {

                fee.setStudentSessionId(fee.getStudentSession().getId());

            }

            if (fee.getFeeHead() != null) {

                fee.setFeeHeadId(fee.getFeeHead().getId());

            }

        });

        return feeOpt;

    }

    // Resolve the transient studentSessionId / feeHeadId into real entities, then save
    public Fee save(Fee fee) {

        if (fee.getStudentSessionId() == null) {

            throw new BusinessException("Student is required.");

        }

        StudentSession studentSession = studentSessionRepo.findById(fee.getStudentSessionId())
                .orElseThrow(() -> new BusinessException("Student session not found."));

        fee.setStudentSession(studentSession);

        if (fee.getFeeHeadId() == null) {

            throw new BusinessException("Fee head is required.");

        }

        FeeHead feeHead = feeHeadRepository.findById(fee.getFeeHeadId())
                .orElseThrow(() -> new BusinessException("Fee head not found."));

        fee.setFeeHead(feeHead);

        return feeRepository.save(fee);

    }

    public void deleteById(Long id) {
        feeRepository.deleteById(id);
    }

    public List<Fee> findByStudentSession(StudentSession studentSession) {
        return feeRepository.findByStudentSession(studentSession);
    }

    public List<Fee> findByAcademicSession(AcademicSession academicSession) {
        return feeRepository.findByStudentSession_AcademicSession(academicSession);
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
