package com.naim.school.sms;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolSettingRepository extends JpaRepository<SchoolSetting, Long> {

    Optional<SchoolSetting> findTopByOrderByIdAsc();

}