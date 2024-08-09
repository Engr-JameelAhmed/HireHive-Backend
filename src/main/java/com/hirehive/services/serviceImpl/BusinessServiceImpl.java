package com.hirehive.services.serviceImpl;

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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements GenericService<BusinessDto, Long> {
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
    public BusinessDto create(BusinessDto business) {
        Business business1 = Business.builder()
                .name(business.getName())
                .industry(business.getIndustry())
                .build();

        User user = userRepository.findById(business.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this ID : " + business.getOwnerId()));
        business1.setOwner_id(user);

        if (business.getInvestmentIds() != null && business.getInvestmentIds().isEmpty()){
            List<Investment> allById = investmentRepository.findAllById(business.getInvestmentIds());
            business1.setInvestments(allById);
        }else{
            business1.setInvestments(Collections.emptyList());
        }
        Business save = businessRepository.save(business1);
        return modelMapper.map(save, BusinessDto.class);
    }

    @Override
    public BusinessDto update(Long id, BusinessDto businessDto) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);
        if (optionalBusiness.isPresent()){
            Business business = optionalBusiness.get();

            business.setName(businessDto.getName());
            business.setIndustry(businessDto.getIndustry());

            User user = userRepository.findById(businessDto.getOwnerId()).orElseThrow(() -> new ResourceNotFoundException("There is no Owner with this ID : " + businessDto.getOwnerId()));
            business.setOwner_id(user);

            if (businessDto.getInvestmentIds() != null && businessDto.getInvestmentIds().isEmpty()){
                List<Investment> allById = investmentRepository.findAllById(businessDto.getInvestmentIds());
                business.setInvestments(allById);
            }else{
                business.setInvestments(Collections.emptyList());
            }
            Business save = businessRepository.save(business);
            return modelMapper.map(save, BusinessDto.class);

        }else{
            throw new RuntimeException("There is no Investment available with this ID : "+ id);
        }
    }
    @Override
    public void delete(Long id) {
        if (!businessRepository.existsById(id)){
            throw new RuntimeException("There is no Business with this ID  to be deleted : "+ id);
        }
        businessRepository.deleteById(id);
    }
}
