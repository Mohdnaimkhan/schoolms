package com.naim.school.sms;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads";
    private static final long MAX_IMAGE_SIZE = 5L * 1024L * 1024L;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    public String uploadStudentPhoto(MultipartFile file) { return upload(file, "students"); }
    public String uploadTeacherPhoto(MultipartFile file) { return upload(file, "teachers"); }
    public String uploadSchoolLogo(MultipartFile file) { return upload(file, "school"); }

    private String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) return null;
        if (file.getSize() > MAX_IMAGE_SIZE) throw new RuntimeException("Image exceeds the 5 MB limit.");

        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String extension = getExtension(original);
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension) || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new RuntimeException("Only JPG, JPEG and PNG image files are allowed.");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) throw new RuntimeException("Uploaded file is not a valid image.");

            String fileName = UUID.randomUUID() + "." + extension;
            Path path = Paths.get(UPLOAD_DIR, folder).normalize();
            Files.createDirectories(path);
            Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed.", e);
        }
    }

    public void delete(String folder, String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path base = Paths.get(UPLOAD_DIR, folder).toAbsolutePath().normalize();
            Path target = base.resolve(fileName).normalize();
            if (!target.startsWith(base)) throw new RuntimeException("Invalid file path.");
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete uploaded file.", e);
        }
    }

    private String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) return "";
        return fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
