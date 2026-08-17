package com.naim.school.sms;

/**
 * BaseEntity
 */

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Soft-delete flag. Records are never physically removed from the
     * database - "deleting" them (via the normal repository.delete()/
     * deleteById() calls already used throughout the app) sets this flag
     * instead, via @SQLDelete on each entity. @SQLRestriction on each
     * entity then hides soft-deleted rows from normal queries, so no
     * existing service/controller code needs to change.
     *
     * This preserves full session-wise history (attendance, fees, exam
     * results, student/teacher session assignments, etc.) even after a
     * record is "deleted" from the UI.
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
