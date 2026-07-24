package com.naim.school.sms;


import java.util.Optional;

import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolSettingService {

    private final SchoolSettingRepository repository;

    /**
     * Get School Settings
     */
    public SchoolSetting getSettings() {

        Optional<SchoolSetting> optional = repository.findById(1L);

        if (optional.isPresent()) {
            return optional.get();
        }                  

        return SchoolSetting.builder()
                .theme("dark")
                .currency("INR")
                .currencySymbol("₹")
                .timeZone("Asia/Kolkata")
                .dateFormat("yyyy-mm-dd")
                .build();
    }

    /**
     * Save / Update School Settings
     */
    public SchoolSetting save(SchoolSetting setting) {

        setting.setId(1L);

        return repository.save(setting);
    }

}