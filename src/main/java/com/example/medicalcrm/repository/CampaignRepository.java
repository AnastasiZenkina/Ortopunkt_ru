package com.example.medicalcrm.repository;
import com.example.medicalcrm.entity.Campaign;
import com.example.medicalcrm.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
