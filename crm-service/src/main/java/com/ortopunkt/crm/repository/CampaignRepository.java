package com.ortopunkt.crm.repository;
import com.ortopunkt.crm.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
