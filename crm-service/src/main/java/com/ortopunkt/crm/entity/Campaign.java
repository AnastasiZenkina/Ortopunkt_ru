package com.ortopunkt.crm.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data


public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
