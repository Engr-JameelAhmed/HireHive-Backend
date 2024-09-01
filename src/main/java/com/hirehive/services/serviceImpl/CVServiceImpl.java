package com.hirehive.services.serviceImpl;

import com.hirehive.dto.CVDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.CV;
import com.hirehive.model.User;
import com.hirehive.repository.CVRespository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CVServiceImpl implements GenericService<CVDto, Long> {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CVRespository cvRepository;
    @Autowired
    private UserRepository userRepository;

    public final String UPLOAD_DIR="D:\\HireHive\\Backend\\HireHive-Backend\\src\\main\\resources\\static\\Resumes\\";
    @Override
    public List<CVDto> getAll() {
        List<CV> all = cvRepository.findAll();
        return all.stream().map(cv -> modelMapper.map(cv, CVDto.class)).collect(Collectors.toList());
    }

    @Override
    public CVDto getById(Long id) {
        Optional<CV> optionalCV = cvRepository.findById(id);
        CV cv = optionalCV.orElseThrow(() -> new ResourceNotFoundException("CV Not Found with this ID"));
        return  modelMapper.map(cv, CVDto.class);
    }

    @Override
    public CVDto create(CVDto entity) throws IOException {
        return null;
    }

    public CVDto createCV(MultipartFile file,CVDto cvDto) throws IOException {

        // Save the file to the file system
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        // Map DTO to entity
        CV cv = new CV();
        cv.setEmployeeId(userRepository.findById(cvDto.getEmployeeId()).orElse(null));
        cv.setPdfFilePath(filePath.toString());

        // Save the CV entity to the database
        cv = cvRepository.save(cv);

        return modelMapper.map(cv,CVDto.class);
    }

    @Override
    public CVDto update(Long id, CVDto cvDto) {
        Optional<CV> optionalCV = cvRepository.findById(id);
        if (optionalCV.isPresent()){
            CV cv = optionalCV.get();

            User user = userRepository.findById(cvDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("There is not User with this Id : "+cvDto.getEmployeeId()));
            cv.setEmployeeId(user);

            CV save = cvRepository.save(cv);
            return modelMapper.map(save, CVDto.class);
        }else{
            throw new RuntimeException("There is not CV with this ID : "+ id);
        }
    }
    @Override
    public void delete(Long id) {

        if (!cvRepository.existsById(id)){
            throw new RuntimeException("There is not CV with this ID : "+ id);
        }
        cvRepository.deleteById(id);
    }


    public File getCVFile(Long cvId) throws IOException {
        CV cv = cvRepository.findById(cvId).orElseThrow(() -> new IllegalArgumentException("Invalid CV ID"));
        Path filePath = Paths.get(cv.getPdfFilePath());

        if (Files.exists(filePath)) {
            return filePath.toFile();
        } else {
            throw new IOException("File not found");
        }
    }
}
