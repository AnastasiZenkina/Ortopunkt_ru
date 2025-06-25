package com.ortopunkt.medicalcrm.dto;
import com.ortopunkt.medicalcrm.entity.Application;
import lombok.Data;
import java.time.LocalDate;

@Data

public class ApplicationResponseDto {
    private Long id;
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

    public static ApplicationResponseDto fromEntity(Application application){
        ApplicationResponseDto dto = new ApplicationResponseDto();
        dto.setId(application.getId());
        if (application.getPatient() != null) {
            dto.setPatientId(application.getPatient().getId());
        }

        if (application.getCampaign() != null) {
            dto.setCampaignId(application.getCampaign().getId());
        }

        dto.setText(application.getText());
        dto.setStatus(application.getStatus());
        dto.setSource(application.getSource());
        dto.setChannel(application.getChannel());
        dto.setTargetOrSpam(application.isTargetOrSpam());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setAdType(application.getAdType());
        dto.setCreative(application.getCreative());

        return dto;
    }
}
