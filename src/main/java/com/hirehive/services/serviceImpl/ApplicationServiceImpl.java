package com.hirehive.services.serviceImpl;

import com.hirehive.dto.ApplicationDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Application;
import com.hirehive.model.Job;
import com.hirehive.model.User;
import com.hirehive.repository.ApplicationRepository;
import com.hirehive.repository.JobRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import com.hirehive.springSecurity.CustomUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements GenericService<ApplicationDto, Long> {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ApplicationDto> getAll() {
        List<Application> all = applicationRepository.findAll();
        return all.stream().map(application -> modelMapper.map(application,ApplicationDto.class)).collect(Collectors.toList());
    }

    @Override
    public ApplicationDto getById(Long id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        Application application = optionalApplication.orElseThrow(() -> new ResourceNotFoundException("There is no Application with this Id : " + id));
        return modelMapper.map(application, ApplicationDto.class);
    }

    @Override
    public ApplicationDto create(ApplicationDto applicationDto) {


        // Fetch Job entity and handle potential error
        Job job = jobRepository.findById(applicationDto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + applicationDto.getJobId()));

        // Fetch the currently logged-in user
        User user = getCurrentUser();

        // Build the Application entity
        Application application = Application.builder()
                .status(applicationDto.getStatus())
                .job(job)
                .employee(user)
                .build();

        // Save and return the Application entity as a DTO
        Application savedApplication = applicationRepository.save(application);
        return modelMapper.map(savedApplication, ApplicationDto.class);
    }

    @Override
    public ApplicationDto update(Long id, ApplicationDto applicationDto) {

        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isPresent()){
            Application application = optionalApplication.get();

            application.setStatus(applicationDto.getStatus());

            Job optionalJob = jobRepository.findById(applicationDto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("There is job present with this Id : "+applicationDto.getJobId()));
            application.setJob(optionalJob);

            User user = userRepository.findById(applicationDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("There is no Employer with this Id : " + applicationDto.getEmployeeId()));
            application.setEmployee(user);

            Application save = applicationRepository.save(application);
            return modelMapper.map(save, ApplicationDto.class);
        }else{
            throw new RuntimeException("There is no Application with this ID : "+ id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!applicationRepository.existsById(id)){
            throw new RuntimeException("There is no Application with this ID for deleting : "+ id);
        }
        applicationRepository.deleteById(id);
    }

    // A method to get the currently logged-in user
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Assuming UserDetails has a method to get the User object or ID
            Long userId = ((CustomUserDetails) userDetails).getUserId(); // or however you get the user ID
            return userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        }
        throw new RuntimeException("User not authenticated");
    }

    public void updateApplicationStatus(Long id){
        applicationRepository.updateStatusToRejected(id);
    }
}
