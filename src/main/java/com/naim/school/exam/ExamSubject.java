package com.naim.school.exam;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.naim.school.sms.BaseEntity;
import com.naim.school.subject.Subject;

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
@SQLDelete(sql = "UPDATE exam_subjects SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted = false")
@Entity
@Table(name = "exam_subjects")
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
