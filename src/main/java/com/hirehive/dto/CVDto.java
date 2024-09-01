package com.hirehive.dto;

import lombok.Data;

@Data
public class CVDto {

    private Long id;
    private Long employeeId;
    private String pdfFileName; // New field to store the PDF file name
}
