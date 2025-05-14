package com.example.medicalcrm.dto;
import lombok.Data;
import java.time.LocalDate;

@Data

public class PostResponseDto {
    private Long id;

    private String title;
    private String platform;
    private LocalDate postDate;
    private Integer reach;
    private Integer likes;
    private Integer comments;
    private Integer shares;
    private Double videoWatchRate;
    private boolean isWeak;
}
