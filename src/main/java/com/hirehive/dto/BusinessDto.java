package com.hirehive.dto;

import lombok.Data;

import java.util.List;

@Data
public class BusinessDto {
    private Long id;
    private Long ownerId;
    private String name;
    private String industry;
    private List<Long> investmentIds;
}
