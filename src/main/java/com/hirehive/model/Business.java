package com.hirehive.model;

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
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner_id;
    private String name;
    private String industry;
    @OneToMany(mappedBy = "business")
    private List<Investment> investments;
}

