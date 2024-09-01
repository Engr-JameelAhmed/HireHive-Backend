package com.hirehive.controller;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.hirehive.dto.CVDto;
import com.hirehive.fileUpload.PDFFileUpload;
import com.hirehive.model.CV;
import com.hirehive.services.serviceImpl.CVServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.http.HttpHeaders;


@RestController
@RequestMapping("/cv")
@CrossOrigin(origins = "*")
public class CVController {

    @Autowired
    private CVServiceImpl cvService;

    @Autowired
    private PDFFileUpload pdfFileUpload;
    @GetMapping("/getAll")
    public List<CVDto> getAllCVs() {
        return cvService.getAll();
    }
    @GetMapping("/{id}")
    public CVDto getCVById(@PathVariable Long id) {
        return cvService.getById(id);
    }


    @PostMapping("/upload")
    public ResponseEntity<CVDto> uploadCV(@RequestParam("file") MultipartFile file,
                                          @RequestParam("employeeId") Long employeeId) {
        CVDto cvDto = new CVDto();
        cvDto.setEmployeeId(employeeId);

        try {
            cvDto = cvService.createCV(file, cvDto);
            return ResponseEntity.ok(cvDto);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PutMapping("/{id}")
    public CVDto updateCV(@PathVariable Long id, @RequestBody CVDto cvDetails) {
        return cvService.update(id, cvDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteCV(@PathVariable Long id) {
        cvService.delete(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadCV(@PathVariable Long id) {
        try {
            File file = cvService.getCVFile(id);

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

}
