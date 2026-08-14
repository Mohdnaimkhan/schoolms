package com.naim.school.sms;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
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

@Controller
@RequestMapping("/settings/backup")
public class BackupController {

    private final String mysqlDumpCommand;
    private final File backupDirectory;

    public BackupController(
            @Value("${backup.mysql-command:mysqldump}") String mysqlDumpCommand,
            @Value("${backup.directory:backups}") String backupDirectory) {
        this.mysqlDumpCommand = mysqlDumpCommand;
        this.backupDirectory = new File(backupDirectory);
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("backupDirectory", backupDirectory.getAbsolutePath());
        model.addAttribute("backupAvailable", isCommandAvailable());
        return "settings/backup";
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download() {
        try {
            if (!backupDirectory.exists() && !backupDirectory.mkdirs()) {
                return ResponseEntity.internalServerError().build();
            }

            String stamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
            File output = new File(backupDirectory, "school-backup-" + stamp + ".sql");

            String url = System.getenv("DB_URL");
            String username = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");
            String database = extractDatabaseName(url == null || url.isBlank() ? "jdbc:mysql://localhost:3306/school_db" : url);

            ProcessBuilder builder = new ProcessBuilder(mysqlDumpCommand, "--single-transaction", "--routines", "--triggers", "-u",
                    username == null ? "school_app" : username, database);
            if (password != null) {
                builder.environment().put("MYSQL_PWD", password);
            }
            builder.redirectErrorStream(true);
            builder.redirectOutput(output);

            Process process = builder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0 || !output.exists() || output.length() == 0) {
                output.delete();
                return ResponseEntity.internalServerError().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + output.getName() + "\"")
                    .body(new FileSystemResource(output));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isCommandAvailable() {
        try {
            return new ProcessBuilder(mysqlDumpCommand, "--version").redirectErrorStream(true).start().waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractDatabaseName(String jdbcUrl) {
        int slash = jdbcUrl.lastIndexOf('/');
        int question = jdbcUrl.indexOf('?', slash);
        return question >= 0 ? jdbcUrl.substring(slash + 1, question) : jdbcUrl.substring(slash + 1);
    }
}
