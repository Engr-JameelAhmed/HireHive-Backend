package com.hirehive.repository;

import com.hirehive.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CVRespository extends JpaRepository<CV, Long> {

}
