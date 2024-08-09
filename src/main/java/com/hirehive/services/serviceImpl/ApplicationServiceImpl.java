package com.hirehive.services.serviceImpl;

import com.hirehive.dto.ApplicationDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Application;
import com.hirehive.model.Job;
import com.hirehive.model.User;
import com.hirehive.repository.ApplicationRepository;
import com.hirehive.repository.JobRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements GenericService<ApplicationDto, Long> {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ApplicationDto> getAll() {
        List<Application> all = applicationRepository.findAll();
        return all.stream().map(application -> modelMapper.map(application,ApplicationDto.class)).collect(Collectors.toList());
    }

    @Override
    public ApplicationDto getById(Long id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        Application application = optionalApplication.orElseThrow(() -> new ResourceNotFoundException("There is no Application with this Id : " + id));
        return modelMapper.map(application, ApplicationDto.class);
    }

    @Override
    public ApplicationDto create(ApplicationDto applicationDto) {

        Application application = Application.builder()
                .status(applicationDto.getStatus())
                .build();

        Job optionalJob = jobRepository.findById(applicationDto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("There is job present with this Id : "+applicationDto.getJobId()));
        application.setJob(optionalJob);

        User user = userRepository.findById(applicationDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("There is no Employer with this Id : " + applicationDto.getEmployeeId()));
        application.setEmployee(user);

        Application save = applicationRepository.save(application);
        return modelMapper.map(save, ApplicationDto.class);
    }

    @Override
    public ApplicationDto update(Long id, ApplicationDto applicationDto) {

        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (optionalApplication.isPresent()){
            Application application = optionalApplication.get();

            application.setStatus(applicationDto.getStatus());

            Job optionalJob = jobRepository.findById(applicationDto.getJobId()).orElseThrow(() -> new ResourceNotFoundException("There is job present with this Id : "+applicationDto.getJobId()));
            application.setJob(optionalJob);

            User user = userRepository.findById(applicationDto.getEmployeeId()).orElseThrow(() -> new ResourceNotFoundException("There is no Employer with this Id : " + applicationDto.getEmployeeId()));
            application.setEmployee(user);

            Application save = applicationRepository.save(application);
            return modelMapper.map(save, ApplicationDto.class);
        }else{
            throw new RuntimeException("There is no Application with this ID : "+ id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!applicationRepository.existsById(id)){
            throw new RuntimeException("There is no Application with this ID for deleting : "+ id);
        }
        applicationRepository.deleteById(id);
    }
}
