package com.hirehive.model;

import com.hirehive.constants.BusinessStatus;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner_id;
    private String ownerName;
    private String description;
    private LocalDateTime createdOn;
    private Long investmentAmount;
    private String name;
    private String industry;
    private Long sharePercent;
    private String proposal;
    @Enumerated(EnumType.STRING)
    private BusinessStatus status;
    @OneToMany(mappedBy = "business")
    private List<Investment> investments;
}

