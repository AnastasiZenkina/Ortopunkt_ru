package com.ortopunkt.dto.response;
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

}
