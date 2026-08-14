package com.naim.school.timetable;

import java.time.LocalTime;

import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.section.Section;
import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;
import com.naim.school.teacher.Teacher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "timetables", uniqueConstraints = {
        @UniqueConstraint(name = "uk_timetable_teacher_slot", columnNames = {"academic_session_id", "teacher_id", "week_day", "period_number"}),
        @UniqueConstraint(name = "uk_timetable_class_slot", columnNames = {"academic_session_id", "class_room_id", "section_id", "week_day", "period_number"})
})
public class Timetable extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day", nullable = false, length = 20)
    private WeekDay weekDay;

    @Column(name = "period_number", nullable = false)
    private Integer periodNumber;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

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
    private Long sectionId;

    @Transient
    private Long subjectId;

    @Transient
    private Long teacherId;

}
