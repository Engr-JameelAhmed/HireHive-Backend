package com.hirehive.repository;


import com.hirehive.dto.AppliedJobsDTO;
import com.hirehive.dto.JobApplicationDTO;
import com.hirehive.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(value = "SELECT ap.employee_id as employeeId,ap.id as applicationId,u.cv,j.title, j.type, j.description, j.category, u.username, u.email, j.posted_date " +
            "FROM job j " +
            "INNER JOIN user u ON u.id = j.employer_id " +
            "INNER JOIN application ap ON j.id = ap.job_id " +
            "WHERE j.employer_id = :employerId and ap.status = 'PENDING'", nativeQuery = true)
    List<JobApplicationDTO> findJobDetailsByEmployerId(@Param("employerId") Long employerId);

    @Query(value = "select * from job j \n" +
            "where j.employer_id = :employerId", nativeQuery = true)
    List<Job> getAllJobsOfLoggedEmployer(@Param("employerId") Long employerId);

    @Query(value = "SELECT j.title AS title, j.companyName AS companyName, j.postedDate AS postedDate, j.location AS location, j.salary AS salary, j.type AS type, j.category AS category, j.description AS description, ap.status AS status " +
            "FROM Job j " +
            "INNER JOIN Application ap ON j.id = ap.job.id " +
            "WHERE ap.employee.id = :employeeId")
    List<AppliedJobsDTO> findAppliedJobsByEmployeeId(@Param("employeeId") Long employeeId);

    @Query(value = "SELECT * FROM job j WHERE " +
            "(j.type = COALESCE(:type, j.type)) " +
            "AND (j.location = COALESCE(:location, j.location)) " +
            "AND (j.category = COALESCE(:category, j.category)) " +
            "AND (j.work_type = COALESCE(:workType, j.work_type))", nativeQuery = true)
    List<Job> findFilteredJobs(
            @Param("type") String type,
            @Param("location") String location,
            @Param("category") String category,
            @Param("workType") String workType
    );

    @Query("SELECT j FROM Job j LEFT JOIN Application a ON j.id = a.job.id AND a.employee.id = :employeeId WHERE a.id IS NULL")
    List<Job> findJobsWithoutApplicationFromEmployee(@Param("employeeId") Long employeeId);

}
