package com.naim.school.exam;

import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "exam_subjects", uniqueConstraints = @UniqueConstraint(name = "uk_exam_subject", columnNames = {"exam_id", "subject_id"}))
public class ExamSubject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "max_marks", nullable = false)
    private Integer maxMarks = 100;

    @Column(name = "pass_marks", nullable = false)
    private Integer passMarks = 33;

    /*
     * ==========================================================
     * FORM BINDING (NOT PERSISTED)
     * ==========================================================
     */

    @Transient
    private Long examId;

    @Transient
    private Long subjectId;

}
