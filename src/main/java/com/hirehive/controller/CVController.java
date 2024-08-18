package com.hirehive.controller;

import com.hirehive.dto.CVDto;
import com.hirehive.model.CV;
import com.hirehive.services.serviceImpl.CVServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cv")
@CrossOrigin(origins = "*")
public class CVController {

    @Autowired
    private CVServiceImpl cvService;
    @GetMapping("/getAll")
    public List<CVDto> getAllCVs() {
        return cvService.getAll();
    }
    @GetMapping("/{id}")
    public CVDto getCVById(@PathVariable Long id) {
        return cvService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<CVDto> createCV(@ModelAttribute CVDto cv, @RequestParam("pdfFile") MultipartFile pdfFile) {
        System.out.println("This is Create");
        CVDto createdCV = cvService.createCV(cv, pdfFile);
        return new ResponseEntity<>(createdCV, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public CVDto updateCV(@PathVariable Long id, @RequestBody CVDto cvDetails) {
        return cvService.update(id, cvDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteCV(@PathVariable Long id) {
        cvService.delete(id);
    }

}
