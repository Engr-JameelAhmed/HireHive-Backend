package com.hirehive.repository;


import com.hirehive.dto.JobApplicationDTO;
import com.hirehive.dto.JobDto;
import com.hirehive.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(value = "SELECT u.id as employeeId,j.title, j.type, j.description, j.category, u.username, u.email, j.posted_date " +
            "FROM job j " +
            "INNER JOIN user u ON u.id = j.employer_id " +
            "INNER JOIN application ap ON j.id = ap.job_id " +
            "WHERE j.employer_id = :employerId", nativeQuery = true)
    List<JobApplicationDTO> findJobDetailsByEmployerId(@Param("employerId") Long employerId);

    @Query(value = "select * from job j \n" +
            "where j.employer_id = :employerId", nativeQuery = true)
    List<Job> getAllJobsOfLoggedEmployer(@Param("employerId") Long employerId);

}
