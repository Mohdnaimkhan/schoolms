package com.naim.school.notice;

import com.naim.school.sms.BusinessException;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository repository;

    public List<Notice> getAll() {

        return repository.findAllByOrderByNoticeDateDesc();

    }

    public List<Notice> getActive() {

        return repository.findByActiveTrueOrderByNoticeDateDesc();

    }

    public Notice getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Notice not found."));

    }

    public void save(Notice notice) {

        repository.save(notice);

    }

    public void delete(Long id) {

        repository.deleteById(id);

    }

    public void changeStatus(Long id) {

        Notice notice = getById(id);

        notice.setActive(!notice.getActive());

        repository.save(notice);

    }

    public long countActive() {

        return repository.countByActiveTrue();

    }

}
