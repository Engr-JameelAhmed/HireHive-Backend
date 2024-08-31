package com.hirehive.dto;

import com.hirehive.constants.InvestmentStatus;
import lombok.Data;

@Data
public class InvestmentDto {
    private Long id;
    private Long investorId;
    private Long businessId;
}
