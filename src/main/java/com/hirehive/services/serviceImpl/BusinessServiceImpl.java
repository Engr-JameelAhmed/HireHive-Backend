package com.hirehive.services.serviceImpl;

import com.hirehive.constants.BusinessStatus;
import com.hirehive.dto.BusinessDto;
import com.hirehive.dto.CVDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Business;
import com.hirehive.model.CV;
import com.hirehive.model.Investment;
import com.hirehive.model.User;
import com.hirehive.repository.BusinessRepository;
import com.hirehive.repository.InvestmentRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements GenericService<BusinessDto, Long> {

    public final String UPLOAD_DIR_PROPOSAL ="D:\\HireHive\\Backend\\HireHive-Backend\\src\\main\\resources\\static\\Proposal\\";

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<BusinessDto> getAll() {
        List<Business> all = businessRepository.findAll();
        return all.stream().map(business -> modelMapper.map(business, BusinessDto.class)).collect(Collectors.toList());
    }
    @Override
    public BusinessDto getById(Long id) {

        Optional<Business> byId = businessRepository.findById(id);
        Business business = byId.orElseThrow(() -> new ResourceNotFoundException("Business Not Found with this ID : " + id));
        return  modelMapper.map(business, BusinessDto.class);
    }
    @Override
    public BusinessDto create( BusinessDto businessDto) {
        // Get the currently logged-in user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            loggedInUsername = userDetails.getUsername(); // or getUserId() if you have a method to get the ID
        } else {
            throw new RuntimeException("No user is currently logged in");
        }

        // Retrieve the User entity based on the logged-in username
        User loggedUser = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this username: " + loggedInUsername));

        // Create the Business entity
        Business business = Business.builder()
                .owner_id(loggedUser)
                .ownerName(loggedUser.getUsername())
                .description(businessDto.getDescription())
                .createdOn(LocalDateTime.now())
                .investmentAmount(businessDto.getInvestmentAmount())
                .name(businessDto.getName())
                .industry(businessDto.getIndustry())
                .sharePercent(businessDto.getSharePercent())
                .build();

        // Retrieve and set investments
        if (businessDto.getInvestmentIds() != null && !businessDto.getInvestmentIds().isEmpty()) {
            List<Investment> investments = investmentRepository.findAllById(businessDto.getInvestmentIds());
            business.setInvestments(investments);
        } else {
            business.setInvestments(Collections.emptyList());
        }

        // Determine the status based on the size of the investments list
        if (business.getInvestments().size() <= 4) {
            business.setStatus(BusinessStatus.PENDING);
        } else {
            business.setStatus(BusinessStatus.ACTIVE);
        }

        // Save the Business entity
        Business savedBusiness = businessRepository.save(business);

        // Return the BusinessDto
        return modelMapper.map(savedBusiness, BusinessDto.class);
    }
    public BusinessDto createBusinessWithProposal(MultipartFile proposal, BusinessDto businessDto) throws IOException {
        String proposalPath = null;

        // Check if the cv is not empty
        if (proposal != null && !proposal.isEmpty()) {
            // Save the file to the file system
            String fileName = proposal.getOriginalFilename();
            proposalPath = Paths.get(UPLOAD_DIR_PROPOSAL + fileName).toString();
            Files.createDirectories(Paths.get(UPLOAD_DIR_PROPOSAL));
            Files.write(Paths.get(proposalPath),proposal.getBytes());
        }

        // Get the currently logged-in user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            loggedInUsername = userDetails.getUsername(); // or getUserId() if you have a method to get the ID
        } else {
            throw new RuntimeException("No user is currently logged in");
        }

        // Retrieve the User entity based on the logged-in username
        User loggedUser = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this username: " + loggedInUsername));

        // Create the Business entity
        Business business = Business.builder()
                .owner_id(loggedUser)
                .ownerName(loggedUser.getUsername())
                .description(businessDto.getDescription())
                .createdOn(LocalDateTime.now())
                .investmentAmount(businessDto.getInvestmentAmount())
                .name(businessDto.getName())
                .industry(businessDto.getIndustry())
                .sharePercent(businessDto.getSharePercent())
                .build();

        // Retrieve and set investments
        if (businessDto.getInvestmentIds() != null && !businessDto.getInvestmentIds().isEmpty()) {
            List<Investment> investments = investmentRepository.findAllById(businessDto.getInvestmentIds());
            business.setInvestments(investments);
        } else {
            business.setInvestments(Collections.emptyList());
        }

        // Set the proposal field only if a file is uploaded
        if (proposalPath != null) {
            business.setProposal(proposalPath);
        }


        // Determine the status based on the size of the investments list
        if (business.getInvestments().size() <= 4) {
            business.setStatus(BusinessStatus.PENDING);
        } else {
            business.setStatus(BusinessStatus.ACTIVE);
        }

        // Save the Business entity
        Business savedBusiness = businessRepository.save(business);

        // Return the BusinessDto
        return modelMapper.map(savedBusiness, BusinessDto.class);
    }
    @Override
    public BusinessDto update(Long id, BusinessDto businessDto) {
        // Get the currently logged-in user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            loggedInUsername = userDetails.getUsername(); // or getUserId() if you have a method to get the ID
        } else {
            throw new RuntimeException("No user is currently logged in");
        }

        // Retrieve the User entity based on the logged-in username
        User loggedUser = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this username: " + loggedInUsername));

        // Find the existing Business entity
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no Business available with this ID: " + id));

        // Update the Business entity fields (excluding createdOn)
        business.setOwner_id(loggedUser);
        business.setOwnerName(businessDto.getOwnerName());
        business.setDescription(businessDto.getDescription());
        // Do not modify createdOn, keep its existing value
        business.setInvestmentAmount(businessDto.getInvestmentAmount());
        business.setName(businessDto.getName());
        business.setIndustry(businessDto.getIndustry());
        business.setSharePercent(businessDto.getSharePercent());

        // Update the owner based on the provided ID
        User user = userRepository.findById(businessDto.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this ID: " + businessDto.getOwnerId()));
        business.setOwner_id(user);

        // Update the investments list
        if (businessDto.getInvestmentIds() != null && !businessDto.getInvestmentIds().isEmpty()) {
            List<Investment> investments = investmentRepository.findAllById(businessDto.getInvestmentIds());
            business.setInvestments(investments);
        } else {
            business.setInvestments(Collections.emptyList());
        }
        // Determine the status based on the size of the investments list
        if (business.getInvestments().size() <= 4) {
            business.setStatus(BusinessStatus.PENDING);
        } else {
            business.setStatus(BusinessStatus.ACTIVE);
        }

        // Save the updated Business entity
        Business updatedBusiness = businessRepository.save(business);

        // Return the updated BusinessDto
        return modelMapper.map(updatedBusiness, BusinessDto.class);
    }
    @Override
    public void delete(Long id) {
        if (!businessRepository.existsById(id)){
            throw new RuntimeException("There is no Business with this ID  to be deleted : "+ id);
        }
        businessRepository.deleteById(id);
    }

    public List<BusinessDto> getAllActiveBusiness() {
        List<Business> all = businessRepository.getAllActiveBusiness();
        return all.stream().map(business -> modelMapper.map(business, BusinessDto.class)).collect(Collectors.toList());
    }
    public List<BusinessDto> getAllPendingBusiness() {
        List<Business> all = businessRepository.getAllPendingBusiness();
        return all.stream().map(business -> modelMapper.map(business, BusinessDto.class)).collect(Collectors.toList());
    }

    public File getProposalFile(Long userId) throws IOException {
        Business businessIsNotPresent = businessRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Business is not present"));
        Path filePath = Paths.get(businessIsNotPresent.getProposal());

        if (Files.exists(filePath)) {
            return filePath.toFile();
        } else {
            throw new IOException("Proposal not found");
        }
    }
}
