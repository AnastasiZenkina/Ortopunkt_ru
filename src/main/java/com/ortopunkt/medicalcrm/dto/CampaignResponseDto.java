package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Campaign;
import lombok.Data;
import java.time.LocalDate;

@Data

public class CampaignResponseDto {
    private Long id;

    private String name;
    private String platform;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budgetPerDay;
    private Double totalSpent;
    private Double ctr;
    private Integer totalLeads;

    public static CampaignResponseDto fromEntity(Campaign campaign) {
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
