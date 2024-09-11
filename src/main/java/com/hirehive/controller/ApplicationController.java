package com.hirehive.controller;

import com.hirehive.dto.ApplicationDto;
import com.hirehive.model.Application;
import com.hirehive.services.serviceImpl.ApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/application")
@RestController
@CrossOrigin(origins = "*")
public class ApplicationController {
    @Autowired
    private ApplicationServiceImpl applicationService;

    @GetMapping("/getAll")
    public List<ApplicationDto> getAllApplication() {
        return applicationService.getAll();
    }

    @GetMapping("/{id}")
    public ApplicationDto getApplicationById(@PathVariable Long id) {
        return applicationService.getById(id);
    }

    @PostMapping("/create")
    public ApplicationDto createApplication(@RequestBody ApplicationDto application) {
        return applicationService.create(application);
    }

    @PutMapping("/{id}")
    public ApplicationDto updateApplication(@PathVariable Long id, @RequestBody ApplicationDto applicationDetails) {
        return applicationService.update(id, applicationDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.delete(id);
    }

//    @GetMapping("/getAllApplications/{id}")
//    public ResponseEntity<List<ApplicationDto>> getAllApplicationOfLoggedEmployer(@PathVariable Long id){
//        List<ApplicationDto> loggedEmployerApplications = applicationService.getLoggedEmployerApplications(id);
//        return ResponseEntity.ok(loggedEmployerApplications);
//    }

    @PutMapping("/{id}/status/reject")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable Long id) {
        applicationService.updateApplicationStatus(id);
        return ResponseEntity.noContent().build();
    }
}
