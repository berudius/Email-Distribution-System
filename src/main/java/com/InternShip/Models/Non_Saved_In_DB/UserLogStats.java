package com.InternShip.Models.Non_Saved_In_DB;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserLogStats {
    private String userName;
    private String userEmail;
    private LocalDateTime first;
    private LocalDateTime last;
    private Long restCount;
    private Long cronCount;

    public UserLogStats(String userName, String userEmail, Timestamp first, Timestamp last, Long restCount, Long cronCount) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.first = first.toLocalDateTime();
        this.last = last.toLocalDateTime();
        this.restCount = restCount;
        this.cronCount = cronCount;
    }

public UserLogStats(){}



}
