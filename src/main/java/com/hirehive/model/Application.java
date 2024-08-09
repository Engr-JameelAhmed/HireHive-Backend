package com.hirehive.model;

import com.hirehive.constants.ApplicationStatus;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

}

