package com.naim.school.activitylog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findAllByOrderByCreatedAtDesc();

    @Query("""
        SELECT a FROM ActivityLog a
        WHERE (:username IS NULL OR :username = '' OR a.username = :username)
          AND (:module IS NULL OR :module = '' OR a.module = :module)
          AND (:action IS NULL OR a.action = :action)
          AND (:fromDate IS NULL OR a.createdAt >= :fromDate)
          AND (:toDate IS NULL OR a.createdAt <= :toDate)
        ORDER BY a.createdAt DESC
        """)
    List<ActivityLog> search(
            @Param("username") String username,
            @Param("module") String module,
            @Param("action") ActivityAction action,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    List<ActivityLog> findTop10ByOrderByCreatedAtDesc();

}
