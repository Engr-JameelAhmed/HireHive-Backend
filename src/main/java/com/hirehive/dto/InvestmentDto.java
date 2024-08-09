package com.hirehive.dto;

import com.hirehive.constants.InvestmentStatus;
import lombok.Data;

@Data
public class InvestmentDto {
    private Long id;
    private Long investorId;
    private Long businessId;
    private Double amount;
    private String proposal;   //  this should also be in pdf format, inshallah will perform this too in near future
    private InvestmentStatus status;
}
