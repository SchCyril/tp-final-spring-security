package com.example.demo.controller;

import com.example.demo.models.JobOffer;
import com.example.demo.models.UserApp;
import com.example.demo.repositories.JobOfferRepository;
import com.example.demo.repositories.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class JobOfferController {


    private final JobOfferRepository jobOfferRepository;

    private final UserAppRepository userAppRepository;

    public JobOfferController(JobOfferRepository jobOfferRepository, UserAppRepository userAppRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.userAppRepository = userAppRepository;
    }


    @GetMapping
    public List<JobOffer> getAllOffers() {
        return jobOfferRepository.findAll();
    }

    // 2️⃣ accessible seulement aux connectés
    @PostMapping
    public JobOffer createOffer(@RequestBody JobOffer offer, Authentication authentication) {
        UserApp user = (UserApp) authentication.getPrincipal();
        offer.setCreatedBy(user);
        return jobOfferRepository.save(offer);
    }

    // 3️⃣ suppression par créateur ou ADMIN
    @DeleteMapping("/{id}")
    public void deleteOffer(@PathVariable Integer id, Authentication authentication) {
        UserApp user = (UserApp) authentication.getPrincipal();
        JobOffer offer = jobOfferRepository.findById(Long.valueOf(id)).orElseThrow();

        if (offer.getCreatedBy().getId().equals(user.getId()) || user.getRole().equals("ADMIN")) {
            jobOfferRepository.delete(offer);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }
    }
}
