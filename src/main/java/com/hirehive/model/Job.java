package com.hirehive.model;

import com.hirehive.constants.JobType;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String companyName;
    private Double salary;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @ManyToOne
    @JoinColumn(name = "employer_id",referencedColumnName = "id")
    private User employer_id;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    // Getters and setters
}

