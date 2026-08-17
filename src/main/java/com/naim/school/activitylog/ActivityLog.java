package com.naim.school.activitylog;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A permanent, append-only audit trail: who did what, and when.
 *
 * Deliberately does NOT extend BaseEntity - activity log rows are never
 * soft-deleted, edited, or hidden. There is no in-app way to remove or
 * alter an entry once written, by design (an audit trail that can be
 * edited or erased from within the app isn't trustworthy).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityAction action;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(length = 500)
    private String description;

    public ActivityLog(String username, ActivityAction action, String module, String description) {
        this.username = username;
        this.action = action;
        this.module = module;
        this.description = description;
    }

}
