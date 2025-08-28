package com.example.demo.repositories;

import com.example.demo.models.JobOffer;
import com.example.demo.models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    List<JobOffer> findByCreatedBy(UserApp userApp);
}
