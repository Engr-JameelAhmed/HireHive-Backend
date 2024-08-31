package com.hirehive.dto;


import com.hirehive.constants.JobCategory;
import com.hirehive.constants.JobType;
import com.hirehive.constants.Locations;
import com.hirehive.constants.WorkType;
import com.hirehive.model.Application;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class JobDto {
    private Long id;
    private String title;
    private String description;
    private String companyName;
    private Double salary;
    private Locations location;
    private LocalDateTime postedDate;
    private JobCategory category;
    private JobType type;
    private WorkType workType;
    private Long employerId;
    private List<Long> applicationIds;
}
