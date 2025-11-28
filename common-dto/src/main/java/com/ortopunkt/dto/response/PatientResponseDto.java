package com.ortopunkt.dto.response;

import lombok.Data;

@Data
public class PatientResponseDto {
    private Long id;
    private String name;
    private String tgId;
    private String username;
    private String paymentStatus;
}