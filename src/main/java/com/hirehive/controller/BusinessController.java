package com.hirehive.controller;

import com.hirehive.dto.BusinessDto;
import com.hirehive.model.Business;
import com.hirehive.repository.BusinessRepository;
import com.hirehive.services.serviceImpl.BusinessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
@CrossOrigin(origins = "*")
public class BusinessController {
    @Autowired
    private BusinessServiceImpl businessService;

    @GetMapping("/getAll")
    public List<BusinessDto> getAllBusinesses() {
        return businessService.getAll();
    }

    @GetMapping("/{id}")
    public BusinessDto getBusinessById(@PathVariable Long id) {
        return businessService.getById(id);
    }

    @PostMapping("/create")
    public BusinessDto createBusiness(@RequestBody BusinessDto business) {
        return businessService.create(business);
    }

    @PutMapping("/{id}")
    public BusinessDto updateBusiness(@PathVariable Long id, @RequestBody BusinessDto businessDetails) {
        return businessService.update(id, businessDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteBusiness(@PathVariable Long id) {
        businessService.delete(id);
    }
    @GetMapping("/activeBusinesses")
    public ResponseEntity<List<BusinessDto>> getAllActiveBusiness() {
        List<BusinessDto> allActiveBusiness = businessService.getAllActiveBusiness();
        return new ResponseEntity<>(allActiveBusiness, HttpStatus.OK);
    }
    @GetMapping("/pendingBusinesses")
    public ResponseEntity<List<BusinessDto>> getAllPendingBusiness() {
        List<BusinessDto> allPendingBusiness = businessService.getAllPendingBusiness();
        return new ResponseEntity<>(allPendingBusiness, HttpStatus.OK);
    }
}
