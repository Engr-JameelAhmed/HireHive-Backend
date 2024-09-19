package com.hirehive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hirehive.dto.BusinessDto;
import com.hirehive.dto.UserDto;
import com.hirehive.model.Business;
import com.hirehive.repository.BusinessRepository;
import com.hirehive.services.serviceImpl.BusinessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BusinessDto createBusiness(
            @RequestPart(value = "file", required = false) MultipartFile proposal,
            @RequestPart("business") String businessDtoJson
    ) throws IOException {
        // Convert the JSON string to UserDto
        ObjectMapper objectMapper = new ObjectMapper();
        BusinessDto businessDto = objectMapper.readValue(businessDtoJson, BusinessDto.class);
        return businessService.createBusinessWithProposal(proposal,businessDto);
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadProposal(@PathVariable Long id) {
        try {
            File file = businessService.getProposalFile(id);

            // Read the file as byte array
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Set the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", file.getName());
            headers.setContentLength(fileContent.length);

            // Return the file as a byte array
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
