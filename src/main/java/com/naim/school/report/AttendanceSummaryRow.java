package com.naim.school.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceSummaryRow {

    private String studentName;
    private String admissionNo;
    private long present;
    private long absent;
    private long leave;

    public long getTotal() {

        return present + absent + leave;

    }

}
