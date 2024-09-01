package com.hirehive.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class UserBusinessInvestmentDTO {


    private String industry;
    private String name;
    private String ownerName;
    private String username;
    private String email;
    private LocalDateTime createdOn;
    private String description;
    private Long investmentAmount;

}
