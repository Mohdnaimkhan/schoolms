package com.naim.school.sms;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.format.DateTimeFormatter;
import java.util.List;
=======

public class AppInfo {
>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57

public final class AppInfo {
    private AppInfo() {}
    public static final String APP_NAME = "School Management System";
<<<<<<< HEAD
=======

    public static final String VERSION = "v1.0.1";

    public static final String RELEASE_DATE = LocalDate.now().toString();
    
    public static final String LAST_UPDATED = LocalDate.now().minusDays(1).toString();

>>>>>>> 1410f37485c3674c8fae4f9f02f79c6ccb358c57
    public static final String DEVELOPER = "Mohd Naim";
    public static final LocalDate RELEASE_DATE_VALUE = LocalDate.now();
    public static final String VERSION = "v" + RELEASE_DATE_VALUE.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    public static final String RELEASE_DATE = RELEASE_DATE_VALUE.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
    public static final String LAST_UPDATED = RELEASE_DATE;
    public static final List<ChangeLog> CHANGELOG = List.of(
        new ChangeLog(VERSION, RELEASE_DATE, "Current build: unified search UI, teacher profile/photo layout, school slogan and date-based versioning."),
        new ChangeLog("v1.0.1", "13 August 2026", "School Management System maintenance release."),
        new ChangeLog("v1.0.0", "01 August 2026", "Initial release with core school management modules.")
    );
    public record ChangeLog(String version, String releaseDate, String description) {}
}
