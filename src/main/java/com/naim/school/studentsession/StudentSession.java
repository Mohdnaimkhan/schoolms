package com.naim.school.studentsession;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;


import com.naim.school.student.Student;
import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.section.Section;
import com.naim.school.sms.BaseEntity;
import com.naim.school.student.StudentStatus;

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
@SQLDelete(sql = "UPDATE student_sessions SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "student_sessions")
public class StudentSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long studentId;

    @Transient
    private Long academicSessionId;

    @Transient
    private Long classRoomId;

    @Transient
    private Long sectionId;

    @Column(name = "roll_number", length = 20)
    private String rollNumber;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "current_session", nullable = false)
    private Boolean currentSession = true;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "status", nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

}