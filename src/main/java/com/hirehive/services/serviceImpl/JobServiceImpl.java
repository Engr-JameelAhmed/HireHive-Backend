package com.hirehive.services.serviceImpl;

import com.hirehive.dto.AppliedJobsDTO;
import com.hirehive.dto.JobApplicationDTO;
import com.hirehive.dto.JobDto;
import com.hirehive.dto.SearchJobsDTO;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Application;
import com.hirehive.model.Job;
import com.hirehive.model.User;
import com.hirehive.repository.ApplicationRepository;
import com.hirehive.repository.JobRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import jakarta.persistence.Query;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements GenericService<JobDto, Long> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ApplicationRepository applicationRepository;
    LocalDateTime currentDateTime = LocalDateTime.now();


    @Override
    public List<JobDto> getAll() {
        List<Job> all = jobRepository.findAll();
        return all.stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    @Override
    public JobDto getById(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        Job job = optionalJob.orElseThrow(() -> new ResourceNotFoundException("Job Not Found with this ID"));
        return  modelMapper.map(job, JobDto.class);
    }

    @Override
    public JobDto create(JobDto jobDto) {
        // Extract the current logged-in user's employerId from the SecurityContext
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + currentUserEmail));

        // Build the Job entity
        Job job = Job.builder()
                .title(jobDto.getTitle())
                .description(jobDto.getDescription())
                .companyName(jobDto.getCompanyName())
                .salary(jobDto.getSalary())
                .location(jobDto.getLocation())
                .postedDate(currentDateTime)
                .type(jobDto.getType())
                .category(jobDto.getCategory())
                .workType(jobDto.getWorkType())
                .employer(user)
                .build();

        // Handle associated applications if any
        if (jobDto.getApplicationIds() != null && !jobDto.getApplicationIds().isEmpty()) {
            List<Application> allApplications = applicationRepository.findAllById(jobDto.getApplicationIds());
            job.setApplications(allApplications);
        } else {
            job.setApplications(Collections.emptyList());
        }

        // Save the job and return the DTO
        Job savedJob = jobRepository.save(job);
        return modelMapper.map(savedJob, JobDto.class);
    }

    @Override
    public JobDto update(Long id, JobDto jobDto) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()){
            Job job = optionalJob.get();

            job.setTitle(jobDto.getTitle());
            job.setDescription(jobDto.getDescription());
            job.setCompanyName(jobDto.getCompanyName());
            job.setSalary(jobDto.getSalary());
            job.setLocation(jobDto.getLocation());
            job.setType(jobDto.getType());
            job.setCategory(jobDto.getCategory());
            job.setWorkType(jobDto.getWorkType());
            User user = userRepository.findById(jobDto.getEmployerId()).orElseThrow(() -> new ResourceNotFoundException("There is not Employer with this Id : "+jobDto.getEmployerId()));
            job.setEmployer(user);

            Job save = jobRepository.save(job);
            return modelMapper.map(save, JobDto.class);
        }else{
            throw new RuntimeException("There is not Job with this ID : "+ id);
        }
    }
    @Override
    public void delete(Long id) {
        if (!jobRepository.existsById(id)){
            throw new RuntimeException("There is no Employer with this Id : "+ id);
        }
        jobRepository.deleteById(id);
    }

    public List<JobApplicationDTO> getJobDetailsByEmployer(Long employerId) {
        return jobRepository.findJobDetailsByEmployerId(employerId);
    }
    public List<JobDto> getAllJobsOfLoggedEmployer(Long employerId) {
        List<Job> allJobsOfLoggedEmployer = jobRepository.getAllJobsOfLoggedEmployer(employerId);
        return allJobsOfLoggedEmployer.stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    public List<JobDto> getSearchedJobs(String type, String location, String category, String workType) {
        List<Job> filteredJobs = jobRepository.findFilteredJobs(type, location, category, workType);
        return filteredJobs.stream().map(job -> modelMapper.map(job,JobDto.class)).collect(Collectors.toList());
    }

    public List<AppliedJobsDTO> getAllAppliedJobsOfLoggedEmployer(Long employerId) {
        return jobRepository.findAppliedJobsByEmployeeId(employerId);
    }

    public List<JobDto> getAllNotAppliedJobs(Long employerId) {
        List<Job> jobsWithoutApplicationFromEmployee = jobRepository.findJobsWithoutApplicationFromEmployee(employerId);
        return jobsWithoutApplicationFromEmployee.stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }



}
