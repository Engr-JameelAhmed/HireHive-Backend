package com.hirehive.repository;

import com.hirehive.dto.UserBusinessInvestmentDTO;
import com.hirehive.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    @Query("SELECT new com.hirehive.dto.UserBusinessInvestmentDTO(b.industry, b.name, b.ownerName, u.username, u.email, b.createdOn, b.description, b.investmentAmount) " +
            "FROM Business b " +
            "JOIN b.investments i " +
            "JOIN b.owner_id u " +
            "WHERE i.investor_id.id = :investorId")
    List<UserBusinessInvestmentDTO> getAllInvestmentsForLoggedUser(@Param("investorId") Long investorId);
}
