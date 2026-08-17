package com.naim.school.sms;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads";

    // Only real image types are accepted for photo/logo uploads.
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp");

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

            if (!ALLOWED_EXTENSIONS.contains(extension)) {

                throw new BusinessException(
                        "Invalid file type. Only JPG, JPEG, PNG and WEBP images are allowed.");

            }

            String contentType = file.getContentType();

            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {

                throw new BusinessException(
                        "Invalid file content. Only JPG, JPEG, PNG and WEBP images are allowed.");

            }

            String fileName = UUID.randomUUID() + "." + extension;

            Path path = Paths.get(UPLOAD_DIR, folder);

            Files.createDirectories(path);

            Files.copy(
                    file.getInputStream(),
                    path.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {

            throw new BusinessException("File Upload Failed.");

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

        // Only allow a bare file name (no directory separators) so a crafted
        // value can never delete files outside the upload folder.
        if (fileName.contains("/") || fileName.contains("\\") || fileName.contains("..")) {

            return;

        }

        try {

            Files.deleteIfExists(

                    Paths.get(UPLOAD_DIR, folder, fileName)

            );

        } catch (IOException e) {

            log.warn("Failed to delete uploaded file: {}/{}", folder, fileName, e);

        }

    }

    /*
     * =========================================
     * Extension
     * =========================================
     */

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {

            return "";

        }

        String ext = fileName.substring(fileName.lastIndexOf('.') + 1)
                .trim()
                .toLowerCase(Locale.ROOT);

        // Strip anything that isn't a simple alphanumeric extension to prevent
        // path traversal / null-byte tricks via a crafted original filename.
        if (!ext.matches("[a-z0-9]+")) {

            return "";

        }

        return ext;

    }

}