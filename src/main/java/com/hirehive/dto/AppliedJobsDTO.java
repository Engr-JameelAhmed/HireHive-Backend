package com.hirehive.dto;

import java.time.LocalDateTime;

public interface AppliedJobsDTO {
    String getTitle();
    String getCompanyName();
    LocalDateTime getPostedDate();
    String getLocation();
    Long getSalary();
    String getType();
    String getCategory();
    String getDescription();
    String getStatus();
}
