package com.naim.school.feestructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.feehead.FeeHead;

public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {

    List<FeeStructure> findByAcademicSession(AcademicSession academicSession);

    List<FeeStructure> findByAcademicSessionAndClassRoom(AcademicSession academicSession, ClassRoom classRoom);

    Optional<FeeStructure> findByAcademicSessionAndClassRoomAndFeeHead(
            AcademicSession academicSession, ClassRoom classRoom, FeeHead feeHead);

    boolean existsByAcademicSessionAndClassRoomAndFeeHead(
            AcademicSession academicSession, ClassRoom classRoom, FeeHead feeHead);

}
