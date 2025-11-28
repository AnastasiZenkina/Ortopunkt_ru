package com.ortopunkt.dto.request;

import lombok.Data;

@Data
public class PatientRequestDto {
    private String name;
    private String tgId;
    private String username;
    private String paymentStatus;
}