package com.naim.school.attendance;


import com.naim.school.academicsession.AcademicSession;
import com.naim.school.classroom.ClassRoom;
import com.naim.school.sms.BaseEntity;
import com.naim.school.student.Student;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "student_id",
                                "attendance_date",
                                "academic_session_id"
                        }
                )
        }
)
public class Attendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @NotNull(message = "Student is required.")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    @NotNull(message = "Classroom is required.")
    private ClassRoom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_session_id", nullable = false)
    @NotNull(message = "Academic session is required.")
    private AcademicSession academicSession;

    @Column(name = "attendance_date", nullable = false)
    @NotNull(message = "Attendance date is required.")
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Attendance status is required.")
    private AttendanceStatus status;

    @Column(length = 255)
    private String remarks;

}
