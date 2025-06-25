package com.ortopunkt.medicalcrm.repository;
import com.ortopunkt.medicalcrm.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
