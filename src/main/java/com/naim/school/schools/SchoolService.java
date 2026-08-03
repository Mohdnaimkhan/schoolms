package com.naim.school.schools;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naim.school.sms.FileStorageService;

@Service
public class SchoolService {

    private final SchoolRepository repository;
    private final FileStorageService fileStorageService;

    public SchoolService(SchoolRepository repository,
            FileStorageService fileStorageService) {

        this.repository = repository;
        this.fileStorageService = fileStorageService;

    }

    /*
     * =========================================
     * Get School
     * =========================================
     */

    public Optional<School> getSchool() {

        return repository.findFirstByOrderByIdAsc();

    }

    /*
     * =========================================
     * Save / Update
     * =========================================
     */

    public void save(School school,
            MultipartFile logoFile,
            MultipartFile signatureFile) {

        /*
         * ==========================
         * EDIT MODE
         * ==========================
         */

        if (school.getId() != null) {

            School oldSchool = repository.findById(school.getId())
                    .orElseThrow(() -> new RuntimeException("School not found."));

            // Keep Old Logo
            if (logoFile == null || logoFile.isEmpty()) {

                school.setLogo(oldSchool.getLogo());

            } else {

                if (oldSchool.getLogo() != null) {

                    fileStorageService.delete(
                            "school",
                            oldSchool.getLogo());

                }

                school.setLogo(
                        fileStorageService.uploadSchoolLogo(logoFile));
            }

            // Keep Old Signature
            if (signatureFile == null || signatureFile.isEmpty()) {

                school.setPrincipalSignature(
                        oldSchool.getPrincipalSignature());

            } else {

                if (oldSchool.getPrincipalSignature() != null) {

                    fileStorageService.delete(
                            "school",
                            oldSchool.getPrincipalSignature());

                }

                school.setPrincipalSignature(
                        fileStorageService.uploadSchoolLogo(signatureFile));
            }

        }

        /*
         * ==========================
         * NEW SCHOOL
         * ==========================
         */

        else {

            if (logoFile != null && !logoFile.isEmpty()) {

                school.setLogo(
                        fileStorageService.uploadSchoolLogo(logoFile));

            }

            if (signatureFile != null && !signatureFile.isEmpty()) {

                school.setPrincipalSignature(
                        fileStorageService.uploadSchoolLogo(signatureFile));

            }

        }

        repository.save(school);
    }
    /*
     * =========================================
     * Utility
     * =========================================
     */

    public boolean schoolExists() {

        return repository.count() > 0;

    }

    public boolean existsBySchoolCode(String schoolCode) {

        return repository.existsBySchoolCode(schoolCode);

    }

}