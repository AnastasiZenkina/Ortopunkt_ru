package com.example.medicalcrm.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data

public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private String text;
    private String status;
    private String paymentStatus; // Платно или По квоте
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private LocalDate createdAt;
    private String adType;
    private String creative;

    @ElementCollection
    private List<String> photoFileIds;

}
