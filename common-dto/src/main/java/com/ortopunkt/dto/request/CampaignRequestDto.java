package com.ortopunkt.dto.request;
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

}
