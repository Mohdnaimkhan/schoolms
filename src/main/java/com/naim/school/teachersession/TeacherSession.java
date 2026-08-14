package com.naim.school.teachersession;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.section.Section;
import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;
import com.naim.school.teacher.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "teacher_sessions", uniqueConstraints = @UniqueConstraint(name = "uk_teacher_session_assignment", columnNames = {"teacher_id", "academic_session_id", "subject_id", "class_room_id", "section_id"}))
public class TeacherSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

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
    private Long teacherId;

    @Transient
    private Long academicSessionId;

    @Transient
    private Long subjectId;

    @Transient
    private Long classRoomId;

    @Transient
    private Long sectionId;

    @Column(name = "current_session", nullable = false)
    private Boolean currentSession = true;

    @Column(name = "remarks", length = 500)
    private String remarks;

}
