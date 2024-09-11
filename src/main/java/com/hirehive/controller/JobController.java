package com.hirehive.controller;

import com.hirehive.dto.AppliedJobsDTO;
import com.hirehive.dto.JobApplicationDTO;
import com.hirehive.dto.JobDto;
import com.hirehive.dto.SearchJobsDTO;
import com.hirehive.services.serviceImpl.JobServiceImpl;
import com.hirehive.springSecurity.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
@CrossOrigin(origins = "*")
public class JobController {
    @Autowired
    private JobServiceImpl jobService;
    @GetMapping("allJobs")
    public List<JobDto> getAllJobs() {
        return jobService.getAll();
    }
    @GetMapping("/{id}")
    public JobDto getJobById(@PathVariable Long id) {
        return jobService.getById(id);
    }
    @PostMapping("/createJob")
    public JobDto createJob(@RequestBody JobDto job) {
        return jobService.create(job);
    }
    @PutMapping("/{id}")
    public JobDto updateJob(@PathVariable Long id, @RequestBody JobDto jobDetails) {
        return jobService.update(id, jobDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobService.delete(id);
    }

    @GetMapping("/employerApplication")
    public ResponseEntity<?> getApplicationOfLoggedEmployer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long currentUserId = userDetails.getUserId();
            List<JobApplicationDTO> jobApplications = jobService.getJobDetailsByEmployer(currentUserId);
            if (jobApplications.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jobApplications);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found or not authenticated");
    }
    @GetMapping("/employerPostedJobs")
    public ResponseEntity<?> getAllJobsOfLoggedEmployer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long currentUserId = userDetails.getUserId();
            List<JobDto> allJobsOfLoggedEmployer = jobService.getAllJobsOfLoggedEmployer(currentUserId);
            if (allJobsOfLoggedEmployer.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allJobsOfLoggedEmployer);
        }
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User details not found");
    }



    @GetMapping("/find-search-jobs")
    public List<JobDto> getSearchedJobs(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String workType
    ){
        return jobService.getSearchedJobs(type,location,category,workType);
    }

    @GetMapping("/applied-jobs-for-current-user")
    public ResponseEntity<?> getAllAppliedJobsOfLoggedEmployer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long currentUserId = userDetails.getUserId();
            List<AppliedJobsDTO> jobApplications = jobService.getAllAppliedJobsOfLoggedEmployer(currentUserId);
            if (jobApplications.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jobApplications);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found or not authenticated");
    }

    @GetMapping("/notapplied-jobs-for-current-user")
    public ResponseEntity<?> getAllNotAppliedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long currentUserId = userDetails.getUserId();
            List<JobDto> allNotAppliedJobs = jobService.getAllNotAppliedJobs(currentUserId);
            if (allNotAppliedJobs.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(allNotAppliedJobs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found or not authenticated");
    }


}
