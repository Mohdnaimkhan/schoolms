package com.naim.school.feestructure;

import com.naim.school.sms.BusinessException;

import java.util.List;

import org.springframework.stereotype.Service;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.academicsession.AcademicSessionRepository;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.classroom.ClassRoomRepository;
import com.naim.school.feehead.FeeHead;
import com.naim.school.feehead.FeeHeadRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeeStructureService {

    private final FeeStructureRepository feeStructureRepository;
    private final AcademicSessionRepository academicSessionRepository;
    private final ClassRoomRepository classRoomRepository;
    private final FeeHeadRepository feeHeadRepository;

    public List<FeeStructure> getAll() {

        return feeStructureRepository.findAll();

    }

    public FeeStructure getById(Long id) {

        FeeStructure structure = feeStructureRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fee structure not found."));

        if (structure.getAcademicSession() != null) {

            structure.setAcademicSessionId(structure.getAcademicSession().getId());

        }

        if (structure.getClassRoom() != null) {

            structure.setClassRoomId(structure.getClassRoom().getId());

        }

        if (structure.getFeeHead() != null) {

            structure.setFeeHeadId(structure.getFeeHead().getId());

        }

        return structure;

    }

    public List<FeeStructure> getByAcademicSession(Long sessionId) {

        return feeStructureRepository.findByAcademicSession(
                academicSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new BusinessException("Academic session not found.")));

    }

    public FeeStructure save(FeeStructure feeStructure) {

        if (feeStructure.getAcademicSessionId() == null) {

            throw new BusinessException("Academic session is required.");

        }

        AcademicSession academicSession = academicSessionRepository.findById(feeStructure.getAcademicSessionId())
                .orElseThrow(() -> new BusinessException("Academic session not found."));

        if (feeStructure.getClassRoomId() == null) {

            throw new BusinessException("Class is required.");

        }

        ClassRoom classRoom = classRoomRepository.findById(feeStructure.getClassRoomId())
                .orElseThrow(() -> new BusinessException("Class not found."));

        if (feeStructure.getFeeHeadId() == null) {

            throw new BusinessException("Fee head is required.");

        }

        FeeHead feeHead = feeHeadRepository.findById(feeStructure.getFeeHeadId())
                .orElseThrow(() -> new BusinessException("Fee head not found."));

        boolean duplicate = feeStructure.getId() == null
                ? feeStructureRepository.existsByAcademicSessionAndClassRoomAndFeeHead(
                        academicSession, classRoom, feeHead)
                : feeStructureRepository.findByAcademicSessionAndClassRoomAndFeeHead(
                        academicSession, classRoom, feeHead)
                        .filter(existing -> !existing.getId().equals(feeStructure.getId()))
                        .isPresent();

        if (duplicate) {

            throw new BusinessException(
                    "A fee structure for this Academic Session, Class and Fee Head already exists.");

        }

        feeStructure.setAcademicSession(academicSession);
        feeStructure.setClassRoom(classRoom);
        feeStructure.setFeeHead(feeHead);

        return feeStructureRepository.save(feeStructure);

    }

    public void delete(Long id) {

        feeStructureRepository.deleteById(id);

    }

}
