package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.CampaignRequestDto;
import com.ortopunkt.dto.response.CampaignResponseDto;
import com.ortopunkt.crm.entity.Campaign;
import com.ortopunkt.crm.repository.CampaignRepository;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    public List<Campaign> getAllCampaigns() {
        log.info("Запрос всех кампаний");
        return campaignRepository.findAll();
    }

    public Optional<Campaign> getCampaignById(Long id) {
        log.info("Получение кампании id=" + id);
        return campaignRepository.findById(id);
    }

    public void deleteCampaign(Long id) {
        log.info("Удаление кампании id=" + id);
        campaignRepository.deleteById(id);
    }

    public CampaignResponseDto create(CampaignRequestDto dto) {
        log.info("Создание новой рекламной кампании: " + dto.getName());
        Campaign campaign = new Campaign();
        campaign.setName(dto.getName());
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        campaign.setBudgetPerDay(dto.getBudgetPerDay());
        campaign.setTotalSpent(dto.getTotalSpent());
        campaign.setCtr(dto.getCtr());
        campaign.setReach(dto.getReach());
        campaign.setImpressions(dto.getImpressions());
        campaign.setSubscribers(dto.getSubscribers());
        campaign.setMessages(dto.getMessages());
        campaign.setCpa(dto.getCpa());

        Campaign saved = campaignRepository.save(campaign);
        return toResponseDto(saved);
    }

    public Optional<CampaignResponseDto> getLatestCampaign() {
        log.info("Получение последней рекламной кампании");
        return campaignRepository.findAll().stream()
                .max(Comparator.comparing(Campaign::getStartDate))
                .map(this::toResponseDto);
    }

    private CampaignResponseDto toResponseDto(Campaign campaign) {
        CampaignResponseDto dto = new CampaignResponseDto();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setBudgetPerDay(campaign.getBudgetPerDay());
        dto.setTotalSpent(campaign.getTotalSpent());
        dto.setCtr(campaign.getCtr());
        dto.setReach(campaign.getReach());
        dto.setImpressions(campaign.getImpressions());
        dto.setSubscribers(campaign.getSubscribers());
        dto.setMessages(campaign.getMessages());
        dto.setCpa(campaign.getCpa());
        return dto;
    }
}