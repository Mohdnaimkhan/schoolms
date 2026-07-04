package com.naim.school.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naim.school.sms.entity.SchoolSetting;

public interface SchoolSettingRepository extends JpaRepository<SchoolSetting, Long> {

}