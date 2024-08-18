package com.hirehive.services.serviceImpl;

import com.hirehive.constants.RoleType;
import com.hirehive.dto.UserDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.*;
import com.hirehive.repository.*;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hirehive.util.GlobalMethods.isEmailValid;
import static com.hirehive.util.GlobalMethods.isValidEmail;

@Service
public class UserServiceImpl implements GenericService<UserDto, Long> {

    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private CVRespository cvRespository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<UserDto> getAll() {
        List<User> all = userRepository.findAll();
        return all.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }
    @Override
    public UserDto getById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return  modelMapper.map(user, UserDto.class);
    }
    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        // Validate if the email actually exists using AbstractAPI
        if (!isEmailValid(userDto.getEmail())) {
            throw new RuntimeException("Invalid or non-existent email address");
        }
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            User newUser = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .role(userDto.getRole())
                    .description(userDto.getDescription())
                    .gender(userDto.getGender())
                    .build();

            // Fetch and set related entities if provided
            if (userDto.getCvIds() != null && !userDto.getCvIds().isEmpty()) {
                List<CV> cvs = cvRespository.findAllById(userDto.getCvIds());
                newUser.setCvs(cvs);
            } else {
                newUser.setCvs(Collections.emptyList());
            }

            if (userDto.getJobIds() != null && !userDto.getJobIds().isEmpty()) {
                List<Job> jobs = jobRepository.findAllById(userDto.getJobIds());
                newUser.setJobs(jobs);
            } else {
                newUser.setJobs(Collections.emptyList());
            }

            if (userDto.getBusinessIds() != null && !userDto.getBusinessIds().isEmpty()) {
                List<Business> businesses = businessRepository.findAllById(userDto.getBusinessIds());
                newUser.setBusinesses(businesses);
            } else {
                newUser.setBusinesses(Collections.emptyList());
            }

            if (userDto.getInvestmentIds() != null && !userDto.getInvestmentIds().isEmpty()) {
                List<Investment> investments = investmentRepository.findAllById(userDto.getInvestmentIds());
                newUser.setInvestments(investments);
            } else {
                newUser.setInvestments(Collections.emptyList());
            }

            // Save the new user
            User savedUser = userRepository.save(newUser);
            new Thread(() -> {
                emailSendingService.sendEmail(userDto.getEmail());
            }).start();

            // Return the saved user mapped to UserDto
            return modelMapper.map(savedUser, UserDto.class);
        } else {
            throw new RuntimeException("User Already Exists");
        }
    }


    @Override
    public UserDto update(Long id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            // Update basic fields
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setDescription(userDto.getDescription());
            user.setGender(userDto.getGender());


            List<CV> cvs = cvRespository.findAllById(userDto.getCvIds());
            user.setCvs(cvs);

            List<Job> jobs = jobRepository.findAllById(userDto.getJobIds());
            user.setJobs(jobs);

            List<Business> business = businessRepository.findAllById(userDto.getBusinessIds());
            user.setBusinesses(business);

            List<Investment> investments = investmentRepository.findAllById(userDto.getInvestmentIds());
            user.setInvestments(investments);
            User save = userRepository.save(user);
            return modelMapper.map(save, UserDto.class);
        }else{
            throw new RuntimeException("User Not Found");
        }
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User Not found with Id : "+ id);
        }
        userRepository.deleteById(id);
    }
}
