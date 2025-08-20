package com.ortopunkt.dto.request;
import lombok.Data;
import java.time.LocalDate;

@Data

public class ApplicationRequestDto {
    private Long patientId;
    private Long campaignId;
    private String text;
    private String status;
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private LocalDate createdAt;
    private String adType;
    private String creative;

}
