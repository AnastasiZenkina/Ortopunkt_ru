package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.CampaignRequestDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.crm.entity.Campaign;
import com.ortopunkt.crm.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService  {

    @Autowired
    private CampaignRepository campaignRepository;

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }

    public Campaign saveCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }

    public CampaignResponseDto create(CampaignRequestDto dto) {
        Campaign campaign = new Campaign();
        campaign.setName(dto.getName());
        campaign.setPlatform(dto.getPlatform());
        campaign.setType(dto.getType());
        campaign.setBudgetPerDay(dto.getBudgetPerDay());
        campaign.setTotalSpent(dto.getTotalSpent());
        campaign.setCtr(dto.getCtr());
        campaign.setTotalLeads(dto.getTotalLeads());
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());

        campaign = campaignRepository.save(campaign);
        return toResponseDto(campaign);
    }

    private CampaignResponseDto toResponseDto(Campaign campaign) {
        CampaignResponseDto dto = new CampaignResponseDto();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setPlatform(campaign.getPlatform());
        dto.setType(campaign.getType());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setBudgetPerDay(campaign.getBudgetPerDay());
        dto.setTotalSpent(campaign.getTotalSpent());
        dto.setCtr(campaign.getCtr());
        dto.setTotalLeads(campaign.getTotalLeads());
        return dto;
    }
}
