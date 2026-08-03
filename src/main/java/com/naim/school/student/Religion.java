package com.naim.school.student;

public enum Religion {

    HINDU("Hindu"),
    MUSLIM("Muslim"),
    SIKH("Sikh"),
    CHRISTIAN("Christian"),
    BUDDHIST("Buddhist"),
    JAIN("Jain"),
    PARSI("Parsi"),
    OTHER("Other");

    private final String displayName;

    Religion(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}