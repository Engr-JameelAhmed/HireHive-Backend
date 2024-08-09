package com.hirehive.services.serviceImpl;

import com.hirehive.dto.InvestmentDto;
import com.hirehive.dto.JobDto;
import com.hirehive.exception.ResourceNotFoundException;
import com.hirehive.model.Business;
import com.hirehive.model.Investment;
import com.hirehive.model.Job;
import com.hirehive.model.User;
import com.hirehive.repository.BusinessRepository;
import com.hirehive.repository.InvestmentRepository;
import com.hirehive.repository.UserRepository;
import com.hirehive.services.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvestmentServiceImpl implements GenericService<InvestmentDto, Long> {

    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<InvestmentDto> getAll() {
        List<Investment> all = investmentRepository.findAll();
        return all.stream().map(investment -> modelMapper.map(investment, InvestmentDto.class)).collect(Collectors.toList());
    }
    @Override
    public InvestmentDto getById(Long id) {
        Optional<Investment> byId = investmentRepository.findById(id);
        Investment investment = byId.orElseThrow(() -> new ResourceNotFoundException("Investment Not Found with this ID : " + id));
        return  modelMapper.map(investment, InvestmentDto.class);
    }
    @Override
    public InvestmentDto create(InvestmentDto investmentDto) {
        Investment investment = Investment.builder()
                .amount(investmentDto.getAmount())
                .proposal(investmentDto.getProposal())
                .status(investmentDto.getStatus())
                .build();


        User user = userRepository.findById(investmentDto.getInvestorId()).orElseThrow(() -> new ResourceNotFoundException("There is no Investor with this ID : " + investmentDto.getInvestorId()));
        investment.setInvestor_id(user);

        Business business = businessRepository.findById(investmentDto.getBusinessId()).orElseThrow(() -> new ResourceNotFoundException("There is No business with this ID where you are doing an investing : " + investmentDto.getBusinessId()));
        investment.setBusiness(business);

        Investment save = investmentRepository.save(investment);
        return modelMapper.map(save, InvestmentDto.class);
    }
    @Override
    public InvestmentDto update(Long id, InvestmentDto investmentDto) {

        Optional<Investment> optionalInves = investmentRepository.findById(id);

        if (optionalInves.isPresent()){
            Investment investment = optionalInves.get();

            investment.setAmount(investmentDto.getAmount());
            investment.setProposal(investmentDto.getProposal());
            investment.setStatus(investmentDto.getStatus());

            User user = userRepository.findById(investmentDto.getInvestorId()).orElseThrow(() -> new ResourceNotFoundException("There is no Investor with this ID : " + investmentDto.getInvestorId()));
            investment.setInvestor_id(user);

            Business business = businessRepository.findById(investmentDto.getBusinessId()).orElseThrow(() -> new ResourceNotFoundException("There is No business with this ID where you are doing an investing : " + investmentDto.getBusinessId()));
            investment.setBusiness(business);

            Investment save = investmentRepository.save(investment);
            return modelMapper.map(save, InvestmentDto.class);
        }
        else{
            throw new RuntimeException("There is No Investment with this ID which you are trying to update : "+ id);
        }
    }
    @Override
    public void delete(Long id) {

        if (!investmentRepository.existsById(id)){
            throw new RuntimeException("There is no Investment with this ID to be deleted : "+ id);
        }
        investmentRepository.deleteById(id);
    }
}
