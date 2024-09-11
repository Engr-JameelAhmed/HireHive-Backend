package com.hirehive.repository;

import com.hirehive.dto.ApplicationDto;
import com.hirehive.model.Application;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE application SET status = 'REJECTED' WHERE id = :id", nativeQuery = true)
    void updateStatusToRejected(@Param("id") Long id);

}
