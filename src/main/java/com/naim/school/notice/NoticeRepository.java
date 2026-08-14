package com.naim.school.notice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByActiveTrueOrderByNoticeDateDesc();

    List<Notice> findAllByOrderByNoticeDateDesc();

    long countByActiveTrue();

}
