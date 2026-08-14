package com.naim.school.result;

import java.math.BigDecimal;

import com.naim.school.exam.ExamSubject;
import com.naim.school.sms.BaseEntity;
import com.naim.school.studentsession.StudentSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

/*
 * A Result row records the marks a student obtained in ONE subject of ONE
 * exam. It links to StudentSession rather than Student directly - this way
 * a mark obtained while the student was in Class 5, Section A during the
 * 2024-25 session stays permanently tied to that placement, even after the
 * student is later promoted to Class 6 for 2025-26. Looking up a student's
 * result for an exam always resolves through their correct historical
 * class/section, never their current one.
 */
@Getter
@Setter
@Entity
@Table(name = "results", uniqueConstraints = @jakarta.persistence.UniqueConstraint(name = "uk_result_student_exam_subject", columnNames = {"student_session_id", "exam_subject_id"}))
public class Result extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_session_id", nullable = false)
    private StudentSession studentSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_subject_id", nullable = false)
    private ExamSubject examSubject;

    @Column(name = "marks_obtained", nullable = false)
    private BigDecimal marksObtained;

    @Column(length = 300)
    private String remarks;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long studentSessionId;

    @Transient
    private Long examSubjectId;

    /**
     * True when the marks obtained meet or exceed the pass marks for this
     * exam subject. Kept as a plain Java method (rather than inline
     * BigDecimal comparison in the template) so the comparison logic is
     * easy to verify and cannot silently misbehave in the view layer.
     */
    public boolean isPassed() {

        if (marksObtained == null || examSubject == null || examSubject.getPassMarks() == null) {

            return false;

        }

        return marksObtained.compareTo(BigDecimal.valueOf(examSubject.getPassMarks())) >= 0;

    }

}
