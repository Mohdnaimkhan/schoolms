package com.naim.school.exam;

import java.time.LocalDate;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
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
@Table(name = "exams")
public class Exam extends BaseEntity {

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoom classRoom;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(length = 500)
    private String description;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long academicSessionId;

    @Transient
    private Long classRoomId;

}
