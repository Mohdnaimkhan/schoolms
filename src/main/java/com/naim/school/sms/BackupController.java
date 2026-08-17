package com.naim.school.sms;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * Lets an administrator download a snapshot of the live SQLite database
 * file as a backup. This is a simple file-copy style backup - it does not
 * touch the running application's data, it only reads the same file the
 * app is already using.
 */
@Controller
@RequestMapping("/settings/backup")
public class BackupController {

    private static final String DB_PATH = "data/school.db";

    @GetMapping
    public String page(Model model) {

        File dbFile = new File(DB_PATH);

        model.addAttribute("dbExists", dbFile.exists());

        if (dbFile.exists()) {

            model.addAttribute("dbSizeKb", dbFile.length() / 1024);

            model.addAttribute("dbLastModified",
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(dbFile.lastModified()),
                            ZoneId.systemDefault()));

        }

        return "settings/backup";

    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download() {

        File dbFile = new File(DB_PATH);

        if (!dbFile.exists()) {

            return ResponseEntity.notFound().build();

        }

        String stamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());

        String filename = "school-backup-" + stamp + ".db";

        Resource resource = new FileSystemResource(dbFile);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);

    }

}
