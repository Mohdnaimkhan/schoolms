package com.naim.school.sms;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    /*
     * =========================================
     * Student Photo
     * =========================================
     */

    public String uploadStudentPhoto(MultipartFile file) {

        return upload(file, "students");

    }

    /*
     * =========================================
     * Teacher Photo
     * =========================================
     */

    public String uploadTeacherPhoto(MultipartFile file) {

        return upload(file, "teachers");

    }

    /*
     * =========================================
     * School Logo
     * =========================================
     */

    public String uploadSchoolLogo(MultipartFile file) {

        return upload(file, "school");

    }

    /*
     * =========================================
     * Common Upload Method
     * =========================================
     */

    private String upload(MultipartFile file, String folder) {

        try {

            if (file == null || file.isEmpty()) {

                return null;

            }

            String extension = getExtension(file.getOriginalFilename());

            String fileName = UUID.randomUUID() + "." + extension;

            Path path = Paths.get(UPLOAD_DIR, folder);

            Files.createDirectories(path);

            Files.copy(
                    file.getInputStream(),
                    path.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {

            throw new RuntimeException("File Upload Failed.");

        }

    }

    /*
     * =========================================
     * Delete
     * =========================================
     */

    public void delete(String folder, String fileName) {

        if (fileName == null || fileName.isBlank()) {

            return;

        }

        try {

            Files.deleteIfExists(

                    Paths.get(UPLOAD_DIR, folder, fileName)

            );

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    /*
     * =========================================
     * Extension
     * =========================================
     */

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {

            return "jpg";

        }

        return fileName.substring(fileName.lastIndexOf('.') + 1);

    }

}