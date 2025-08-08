package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Campaign;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CampaignRequestDto {
    private String name;
    private String platform;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budgetPerDay;
    private Double totalSpent;
    private Double ctr;
    private Integer totalLeads;

    public Campaign toEntity() {
        Campaign campaign = new Campaign();
        campaign.setName(this.name);
        campaign.setPlatform(this.platform);
        campaign.setType(this.type);
        campaign.setBudgetPerDay(this.budgetPerDay);
        campaign.setTotalSpent(this.totalSpent);
        campaign.setCtr(this.ctr);
        campaign.setTotalLeads(this.totalLeads);
        campaign.setStartDate(this.startDate);
        campaign.setEndDate(this.endDate);

        return campaign;
    }
}
