package com.ortopunkt.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ApplicationResponseDto {
    private Long id;
    private PatientResponseDto patient;
    private String text;
    private String status;
    private LocalDate createdAt;
    private boolean answeredByHuman;
    private boolean answeredByAi;
    private List<String> photoFileIds;
}