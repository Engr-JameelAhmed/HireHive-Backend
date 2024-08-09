package com.hirehive.controller;

import com.hirehive.dto.JobDto;
import com.hirehive.model.Job;
import com.hirehive.services.serviceImpl.JobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
}
