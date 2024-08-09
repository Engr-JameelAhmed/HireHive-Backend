package com.hirehive.controller;

import com.hirehive.dto.InvestmentDto;
import com.hirehive.services.serviceImpl.InvestmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investment")
@CrossOrigin(origins = "*")
public class InvestmentController {
    @Autowired
    private InvestmentServiceImpl investmentService;
    @GetMapping("allInvestment")
    public List<InvestmentDto> getAllInvestments() {
        return investmentService.getAll();
    }
    @GetMapping("/{id}")
    public InvestmentDto getJobById(@PathVariable Long id) {
        return investmentService.getById(id);
    }
    @PostMapping("/createInvestment")
    public InvestmentDto createInvestment(@RequestBody InvestmentDto investment) {
        return investmentService.create(investment);
    }
    @PutMapping("/{id}")
    public InvestmentDto updateInvestment(@PathVariable Long id, @RequestBody InvestmentDto investmentDetails) {
        return investmentService.update(id, investmentDetails);
    }
    @DeleteMapping("/{id}")
    public void deleteInvestment(@PathVariable Long id) {
        investmentService.delete(id);
    }
}
