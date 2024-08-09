package com.hirehive.dto;

import com.hirehive.constants.ApplicationStatus;
import lombok.Data;

@Data
public class ApplicationDto {
    private Long id;
    private Long jobId;
    private Long employeeId;
    private ApplicationStatus status;
}
