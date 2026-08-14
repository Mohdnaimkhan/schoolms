package com.naim.school.report;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResultSummary {

    private String subjectName;
    private int maxMarks;
    private long studentsAppeared;
    private long passCount;
    private long failCount;
    private BigDecimal averageMarks;

}
