package com.hirehive.controller;

import com.hirehive.dto.CVDto;
import com.hirehive.model.CV;
import com.hirehive.services.serviceImpl.CVServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CVDto createCV(@RequestBody CVDto cv) {
        System.out.println("This is Create");
        return cvService.create(cv);
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
