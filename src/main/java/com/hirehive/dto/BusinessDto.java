package com.hirehive.dto;

import com.hirehive.constants.BusinessStatus;
import com.hirehive.model.Investment;
import com.hirehive.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BusinessDto {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private String description;
    private LocalDateTime createdOn;
    private String name;
    private Long investmentAmount;
    private Long sharePercent;
    private String industry;
    private BusinessStatus status;
    private List<Long> investmentIds;
    private String proposal;
}
