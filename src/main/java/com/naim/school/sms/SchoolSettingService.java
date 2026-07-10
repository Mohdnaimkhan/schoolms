package com.naim.school.sms;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolSettingService {

    private final SchoolSettingRepository repository;

    public SchoolSetting save(SchoolSetting setting) {
        return repository.save(setting);
    }

    public SchoolSetting update(Long id, SchoolSetting setting) {

        SchoolSetting existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("School Setting not found"));

        existing.setSchoolName(setting.getSchoolName());
        existing.setSchoolCode(setting.getSchoolCode());
        existing.setTagline(setting.getTagline());
        existing.setRegistrationNumber(setting.getRegistrationNumber());

        existing.setLogoPath(setting.getLogoPath());
        existing.setFaviconPath(setting.getFaviconPath());

        existing.setPhone(setting.getPhone());
        existing.setAlternatePhone(setting.getAlternatePhone());
        existing.setEmail(setting.getEmail());
        existing.setWebsite(setting.getWebsite());

        existing.setAddress(setting.getAddress());
        existing.setCity(setting.getCity());
        existing.setState(setting.getState());
        existing.setCountry(setting.getCountry());
        existing.setPinCode(setting.getPinCode());

        existing.setAcademicSession(setting.getAcademicSession());
        existing.setPrincipalName(setting.getPrincipalName());

        existing.setCurrency(setting.getCurrency());
        existing.setCurrencySymbol(setting.getCurrencySymbol());
        existing.setTimeZone(setting.getTimeZone());
        existing.setDateFormat(setting.getDateFormat());
        existing.setTheme(setting.getTheme());

        existing.setFooterText(setting.getFooterText());
        existing.setCopyrightText(setting.getCopyrightText());

        existing.setFacebook(setting.getFacebook());
        existing.setInstagram(setting.getInstagram());
        existing.setTwitter(setting.getTwitter());
        existing.setYoutube(setting.getYoutube());
        existing.setLinkedin(setting.getLinkedin());

        return repository.save(existing);
    }

    public SchoolSetting getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("School Setting not found"));
    }

    public SchoolSetting getSetting() {
        return repository.findTopByOrderByIdAsc()
                .orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}