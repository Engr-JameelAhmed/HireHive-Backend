package com.hirehive.repository;

import com.hirehive.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {


    @Query(value = "select * from business b \n" +
            "where b.status = 'ACTIVE'",
    nativeQuery = true)
    List<Business> getAllActiveBusiness();

    @Query(value = "select * from business b \n" +
            "where b.status = 'PENDING'",
            nativeQuery = true)
    List<Business> getAllPendingBusiness();
}
