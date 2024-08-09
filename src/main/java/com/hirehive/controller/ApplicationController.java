package com.hirehive.controller;

import com.hirehive.dto.ApplicationDto;
import com.hirehive.model.Application;
import com.hirehive.services.serviceImpl.ApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
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
}
