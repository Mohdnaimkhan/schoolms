package com.naim.school.feestructure;

import java.math.BigDecimal;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.feehead.FeeHead;
import com.naim.school.sms.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fee_structures")
public class FeeStructure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_head_id", nullable = false)
    private FeeHead feeHead;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long academicSessionId;

    @Transient
    private Long classRoomId;

    @Transient
    private Long feeHeadId;

}
