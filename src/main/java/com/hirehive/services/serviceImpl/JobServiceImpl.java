package com.hirehive.services.serviceImpl;

import com.hirehive.dto.CVDto;
import com.hirehive.dto.JobDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Application;
import com.hirehive.model.CV;
import com.hirehive.model.Job;
import com.hirehive.model.User;
import com.hirehive.repository.ApplicationRepository;
import com.hirehive.repository.JobRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements GenericService<JobDto, Long> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Override
    public List<JobDto> getAll() {
        List<Job> all = jobRepository.findAll();
        return all.stream().map(job -> modelMapper.map(job, JobDto.class)).collect(Collectors.toList());
    }

    @Override
    public JobDto getById(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        Job job = optionalJob.orElseThrow(() -> new ResourceNotFoundException("Job Not Found with this ID"));
        return  modelMapper.map(job, JobDto.class);
    }

    @Override
    public JobDto create(JobDto jobDto) {
        Job job = Job.builder()
                .title(jobDto.getTitle())
                .description(jobDto.getDescription())
                .companyName(jobDto.getCompanyName())
                .salary(jobDto.getSalary())
                .location(jobDto.getLocation())
                .type(jobDto.getType())
                .build();

        User user = userRepository.findById(jobDto.getEmployerId()).orElseThrow(() -> new ResourceNotFoundException("Employer Not Found with this ID : "+ jobDto.getEmployerId()));
        job.setEmployer_id(user);

        if (jobDto.getApplicationIds() != null && !jobDto.getApplicationIds().isEmpty()){
            List<Application> allApplications = applicationRepository.findAllById(jobDto.getApplicationIds());
            job.setApplications(allApplications);
        }else{
            job.setApplications(Collections.emptyList());
        }
        Job save = jobRepository.save(job);
        return modelMapper.map(save,JobDto.class);
    }

    @Override
    public JobDto update(Long id, JobDto jobDto) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()){
            Job job = optionalJob.get();

            job.setTitle(jobDto.getTitle());
            job.setDescription(jobDto.getDescription());
            job.setCompanyName(jobDto.getCompanyName());
            job.setSalary(jobDto.getSalary());
            job.setLocation(jobDto.getLocation());
            job.setType(jobDto.getType());


            User user = userRepository.findById(jobDto.getEmployerId()).orElseThrow(() -> new ResourceNotFoundException("There is not Employer with this Id : "+jobDto.getEmployerId()));
            job.setEmployer_id(user);

            Job save = jobRepository.save(job);
            return modelMapper.map(save, JobDto.class);
        }else{
            throw new RuntimeException("There is not Job with this ID : "+ id);
        }
    }

    @Override
    public void delete(Long id) {
        if (!jobRepository.existsById(id)){
            throw new RuntimeException("There is no Employer with this Id : "+ id);
        }
        jobRepository.deleteById(id);
    }
}
