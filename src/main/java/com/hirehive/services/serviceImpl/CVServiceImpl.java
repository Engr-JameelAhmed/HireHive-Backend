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
    public CVDto create(CVDto cvDto) {
        CV cv = CV.builder()
                .title(cvDto.getTitle())
                .content(cvDto.getContent())
                .build();

        User userId = userRepository.findById(cvDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found with this ID : "+ cvDto.getEmployeeId()));
        cv.setEmployeeId(userId);

        CV save = cvRepository.save(cv);
        return modelMapper.map(save,CVDto.class);
    }

    @Override
    public CVDto update(Long id, CVDto cvDto) {
        Optional<CV> optionalCV = cvRepository.findById(id);
        if (optionalCV.isPresent()){
            CV cv = optionalCV.get();

            cv.setTitle(cvDto.getTitle());
            cv.setContent(cv.getContent());

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
}
