package com.hirehive.model;

import jakarta.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "id")
    private User employeeId;

    private String pdfFilePath; // New field to store the file path

}

