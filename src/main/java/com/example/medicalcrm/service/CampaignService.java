package com.example.medicalcrm.service;
import com.example.medicalcrm.entity.Campaign;
import com.example.medicalcrm.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public List<Campaign> getAllCampains() {
        return campaignRepository.findAll();
    }

    public Optional<Campaign> getCampainById(Long id) {
        return campaignRepository.findById(id);
    }

    public Campaign saveCampain(Campaign campain) {
        return campaignRepository.save(campain);
    }

    public void deleteCampain(Long id) {
        campaignRepository.deleteById(id);
    }
}
