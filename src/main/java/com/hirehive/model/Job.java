package com.hirehive.model;

import com.hirehive.constants.JobCategory;
import com.hirehive.constants.JobType;
import com.hirehive.constants.WorkType;
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
    private JobCategory category;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @ManyToOne
    @JoinColumn(name = "employer_id",referencedColumnName = "id")
    private User employer_id;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

    // Getters and setters
}

