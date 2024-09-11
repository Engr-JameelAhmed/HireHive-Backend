package com.hirehive.dto;

import java.time.LocalDateTime;

public interface JobApplicationDTO {
    Long getApplicationId();
    Long getEmployeeId();
    String getTitle();
    String getType();
    String getDescription();
    String getCategory();
    String getUsername();
    String getEmail();
    String getCV();
    LocalDateTime getPostedDate();
}
