package com.hirehive.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class SearchJobsDTO {

    private String jobType;
    private String jobLocation;
    private String jobCategory;
    private String jobWorkType;


}
