package com.naim.school.sms;

import java.time.LocalDate;

public class AppInfo {

    public static final String APP_NAME = "School Management System";

    public static final String VERSION = "v1.0.1";

    public static final String RELEASE_DATE = LocalDate.now().toString();
    
    public static final String LAST_UPDATED = LocalDate.now().minusDays(1).toString();

    public static final String DEVELOPER = "Mohd Naim";

}