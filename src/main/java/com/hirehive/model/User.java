package com.hirehive.model;

import com.hirehive.constants.Gender;
import com.hirehive.constants.RoleType;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    private String cv;

    @Enumerated(EnumType.STRING)
    private RoleType role;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "employeeId")
    private List<CV> cvs;

    @OneToMany(mappedBy = "employer")
    private List<Job> jobs;

    @OneToMany(mappedBy = "owner_id")
    private List<Business> businesses;

    @OneToMany(mappedBy = "investor_id")
    private List<Investment> investments;

    @OneToMany(mappedBy = "employee")
    private List<Application> applications;

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + "'}";
    }
}



