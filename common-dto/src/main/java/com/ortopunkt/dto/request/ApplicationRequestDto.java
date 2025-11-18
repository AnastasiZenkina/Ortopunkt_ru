package com.ortopunkt.dto.request;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data

public class ApplicationRequestDto {
    private Long patientId;
    private Long campaignId;
    private String text;
    private String source;
    private String channel;
    private boolean targetOrSpam;
    private String adType;
    private String creative;
    private String username;
    private String fullName;
    private Long chatId;
    private List<String> photoFileIds;
}
