package com.hirehive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hirehive.constants.JobCategory;
import com.hirehive.constants.JobType;
import com.hirehive.constants.Locations;
import com.hirehive.constants.WorkType;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    private LocalDateTime postedDate;

    @Enumerated(EnumType.STRING)
    private Locations location;
    @Enumerated(EnumType.STRING)
    private JobCategory category;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @ManyToOne
    @JoinColumn(name = "employer_id",nullable = false)
    private User employer;

    @OneToMany(mappedBy = "job")
    @JsonIgnore // This will prevent recursion during JSON serialization
    private List<Application> applications;

    @Override
    public String toString() {
        return "Job{id=" + id + ", title='" + title + "', companyName='" + companyName + "', location=" + location + "}";
    }
// Getters and setters
}

