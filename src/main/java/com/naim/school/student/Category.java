package com.naim.school.student;

public enum Category {

    GENERAL("General"),
    OBC("OBC"),
    EWS("EWS"),
    SC("SC"),
    ST("ST"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}