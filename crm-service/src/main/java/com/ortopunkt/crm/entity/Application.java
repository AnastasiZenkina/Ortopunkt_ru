package com.ortopunkt.crm.entity;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String text;
    private String status;
    private LocalDate createdAt;

    private boolean answeredByHuman = false;
    private boolean answeredByAi = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "application_photo_file_ids", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "photo_file_ids")
    private List<String> photoFileIds;
}