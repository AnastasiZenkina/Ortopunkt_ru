package com.ortopunkt.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class CampaignResponseDto {

    private Long id;
    private String name;
    private LocalDate startDate;       // дата начала кампании
    private LocalDate endDate;         // дата окончания кампании
    private Double budgetPerDay;       // дневной бюджет
    private Double totalSpent;         // общие затраты
    private Double ctr;                // CTR кампании

    private Long reach;               // охват рекламы
    private Long impressions;         // показы
    private Long subscribers;         // подписки с рекламы
    private Long messages;           // сообщения с рекламы
    private Double cpa;              // цена подписчика (CPA)
    private Map<String, Double> creativeStats; // ID → CTR
    private Map<String, Integer> creativeSubs; // ID → подписчики
}