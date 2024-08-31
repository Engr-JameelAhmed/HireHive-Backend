package com.hirehive.model;

import com.hirehive.constants.InvestmentStatus;
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
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private User investor_id;

    @ManyToOne
    @JoinColumn(name = "business_id" , referencedColumnName = "id")
    private Business business;



}

