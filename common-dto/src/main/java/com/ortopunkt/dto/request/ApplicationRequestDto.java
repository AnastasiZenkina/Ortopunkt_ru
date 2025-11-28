package com.ortopunkt.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ApplicationRequestDto {
    private Long patientId;
    private String text;
    private List<String> photoFileIds;
    private String tgId;
    private String username;
    private String name;
}