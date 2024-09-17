package com.hirehive.services.serviceImpl;

import com.hirehive.dto.UserDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.*;
import com.hirehive.repository.*;
import com.hirehive.services.GenericService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hirehive.util.GlobalMethods.isEmailValid;

@Service
public class UserServiceImpl implements GenericService<UserDto, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    public final String UPLOAD_DIR_CV ="D:\\HireHive\\Backend\\HireHive-Backend\\src\\main\\resources\\static\\Resumes\\";
    public final String UPLOAD_DIR_PROPOSAL ="D:\\HireHive\\Backend\\HireHive-Backend\\src\\main\\resources\\static\\Proposal\\";


    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private CVRespository cvRespository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public List<UserDto> getAll() {
        List<User> all = userRepository.findAll();
        return all.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }
    @Override
    public UserDto getById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return  modelMapper.map(user, UserDto.class);
    }
    @Transactional
    @Override
    public UserDto create(UserDto userDto) throws IOException {
        // Validate if the email actually exists using AbstractAPI
        if (!isEmailValid(userDto.getEmail())) {
            throw new RuntimeException("Invalid or non-existent email address");
        }
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            User newUser = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .role(userDto.getRole())
                    .gender(userDto.getGender())
                    .build();

            // Fetch and set related entities if provided
            if (userDto.getCvIds() != null && !userDto.getCvIds().isEmpty()) {
                List<CV> cvs = cvRespository.findAllById(userDto.getCvIds());
                newUser.setCvs(cvs);
            } else {
                newUser.setCvs(Collections.emptyList());
            }

            if (userDto.getJobIds() != null && !userDto.getJobIds().isEmpty()) {
                List<Job> jobs = jobRepository.findAllById(userDto.getJobIds());
                newUser.setJobs(jobs);
            } else {
                newUser.setJobs(Collections.emptyList());
            }

            if (userDto.getBusinessIds() != null && !userDto.getBusinessIds().isEmpty()) {
                List<Business> businesses = businessRepository.findAllById(userDto.getBusinessIds());
                newUser.setBusinesses(businesses);
            } else {
                newUser.setBusinesses(Collections.emptyList());
            }

            if (userDto.getInvestmentIds() != null && !userDto.getInvestmentIds().isEmpty()) {
                List<Investment> investments = investmentRepository.findAllById(userDto.getInvestmentIds());
                newUser.setInvestments(investments);
            } else {
                newUser.setInvestments(Collections.emptyList());
            }

            // Save the new user
            User savedUser = userRepository.save(newUser);
            new Thread(() -> {
                emailSendingService.sendEmail(userDto.getEmail());
            }).start();

            // Return the saved user mapped to UserDto
            return modelMapper.map(savedUser, UserDto.class);
        } else {
            throw new RuntimeException("User Already Exists");
        }
    }
    @Transactional
    public UserDto createUser(MultipartFile cv,MultipartFile proposal, UserDto userDto) throws IOException {
        String cvPath = null;
        String proposalPath = null;

        // Check if the cv is not empty
        if (cv != null && !cv.isEmpty()) {
            // Save the file to the file system
            String fileName = cv.getOriginalFilename();
            cvPath = Paths.get(UPLOAD_DIR_CV + fileName).toString();
            Files.createDirectories(Paths.get(UPLOAD_DIR_CV));
            Files.write(Paths.get(cvPath), cv.getBytes());
        }
        // Check if the proposal is not empty
        if (proposal != null && !proposal.isEmpty()) {
            // Save the file to the file system
            String fileName = proposal.getOriginalFilename();
            proposalPath = Paths.get(UPLOAD_DIR_PROPOSAL + fileName).toString();
            Files.createDirectories(Paths.get(UPLOAD_DIR_PROPOSAL));
            Files.write(Paths.get(proposalPath), proposal.getBytes());
        }

        // Validate if the email actually exists using AbstractAPI
        if (!isEmailValid(userDto.getEmail())) {
            throw new RuntimeException("Invalid or non-existent email address");
        }

        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent()) {
            User.UserBuilder userBuilder = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .role(userDto.getRole())
                    .gender(userDto.getGender());

            // Set the cv field only if a file is uploaded
            if (cvPath != null) {
                userBuilder.cv(cvPath);
            }
            // Set the proposal field only if a file is uploaded
            if (proposalPath != null) {
                userBuilder.proposal(proposalPath);
            }

            User newUser = userBuilder.build();

            // Fetch and set related entities if provided
            if (userDto.getCvIds() != null && !userDto.getCvIds().isEmpty()) {
                List<CV> cvs = cvRespository.findAllById(userDto.getCvIds());
                newUser.setCvs(cvs);
            } else {
                newUser.setCvs(Collections.emptyList());
            }

            if (userDto.getJobIds() != null && !userDto.getJobIds().isEmpty()) {
                List<Job> jobs = jobRepository.findAllById(userDto.getJobIds());
                newUser.setJobs(jobs);
            } else {
                newUser.setJobs(Collections.emptyList());
            }

            if (userDto.getBusinessIds() != null && !userDto.getBusinessIds().isEmpty()) {
                List<Business> businesses = businessRepository.findAllById(userDto.getBusinessIds());
                newUser.setBusinesses(businesses);
            } else {
                newUser.setBusinesses(Collections.emptyList());
            }

            if (userDto.getInvestmentIds() != null && !userDto.getInvestmentIds().isEmpty()) {
                List<Investment> investments = investmentRepository.findAllById(userDto.getInvestmentIds());
                newUser.setInvestments(investments);
            } else {
                newUser.setInvestments(Collections.emptyList());
            }

            // Save the new user
            User savedUser = userRepository.save(newUser);
            new Thread(() -> {
                emailSendingService.sendEmail(userDto.getEmail());
            }).start();

            // Return the saved user mapped to UserDto
            return modelMapper.map(savedUser, UserDto.class);
        } else {
            throw new RuntimeException("User Already Exists");
        }
    }

    public UserDto updateUserCv(MultipartFile file, UserDto userDto) throws IOException {
        String filePath = null;

        // Check if the file is not empty
        if (file != null && !file.isEmpty()) {
            // Save the file to the file system
            String fileName = file.getOriginalFilename();
            filePath = Paths.get(UPLOAD_DIR_CV, fileName).toString();
            Files.createDirectories(Paths.get(UPLOAD_DIR_CV));
            Files.write(Paths.get(filePath), file.getBytes());
        }

        // Find the user by email
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User does not exist");
        }

        // Retrieve the existing user
        User existingUser = optionalUser.get();

        // Update the CV field if a new file was uploaded
        if (filePath != null) {
            existingUser.setCv(filePath);
        }

        // Save the updated user
        User updatedUser = userRepository.save(existingUser);

        // Map the updated user to UserDto and return
        return modelMapper.map(updatedUser, UserDto.class);
    }
    public UserDto updateUserProposal(MultipartFile proposal, UserDto userDto) throws IOException {
        String proposalPath = null;


        // Check if the proposal is not empty
        if (proposal != null && !proposal.isEmpty()) {
            // Save the file to the file system
            String fileName = proposal.getOriginalFilename();
            proposalPath = Paths.get(UPLOAD_DIR_PROPOSAL, fileName).toString();
            Files.createDirectories(Paths.get(UPLOAD_DIR_PROPOSAL));
            Files.write(Paths.get(proposalPath), proposal.getBytes());
        }

        // Find the user by email
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User does not exist");
        }

        // Retrieve the existing user
        User existingUser = optionalUser.get();


        // Update the PROPOSAL field if a new file was uploaded
        if (proposalPath != null) {
            existingUser.setProposal(proposalPath);
        }

        // Save the updated user
        User updatedUser = userRepository.save(existingUser);

        // Map the updated user to UserDto and return
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {

        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();

            // Update basic fields
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setGender(userDto.getGender());


            List<CV> cvs = cvRespository.findAllById(userDto.getCvIds());
            user.setCvs(cvs);

            List<Job> jobs = jobRepository.findAllById(userDto.getJobIds());
            user.setJobs(jobs);

            List<Business> business = businessRepository.findAllById(userDto.getBusinessIds());
            user.setBusinesses(business);

            List<Investment> investments = investmentRepository.findAllById(userDto.getInvestmentIds());
            user.setInvestments(investments);
            User save = userRepository.save(user);
            return modelMapper.map(save, UserDto.class);
        }else{
            throw new RuntimeException("User Not Found");
        }
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User Not found with Id : "+ id);
        }
        userRepository.deleteById(id);
    }

    // Fetch user by email
    public UserDto getUserByEmail(String email) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Convert User entity to UserDto if necessary
            return modelMapper.map(user, UserDto.class);
        } else {
            throw new IOException("User not found with email: " + email);
        }
    }


}
