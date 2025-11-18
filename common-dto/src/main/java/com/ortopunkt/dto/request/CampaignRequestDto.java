package com.ortopunkt.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CampaignRequestDto {

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

}
