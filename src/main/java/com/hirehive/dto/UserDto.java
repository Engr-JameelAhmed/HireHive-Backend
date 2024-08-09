package com.hirehive.dto;

import com.hirehive.constants.Gender;
import com.hirehive.constants.RoleType;
import com.hirehive.model.Business;
import com.hirehive.model.CV;
import com.hirehive.model.Investment;
import com.hirehive.model.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private RoleType role;
    private String description;
    private Gender gender;
    private List<Long> cvIds;
    private List<Long> jobIds;
    private List<Long> businessIds;
    private List<Long> investmentIds;

}

