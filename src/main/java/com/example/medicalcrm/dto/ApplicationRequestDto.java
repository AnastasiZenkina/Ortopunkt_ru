package com.example.medicalcrm.dto;
import com.example.medicalcrm.entity.Application;
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

    public Application toEntity(){
        Application application = new Application();
        application.setText(this.text);
        application.setSource(this.source);
        application.setChannel(this.channel);
        application.setTargetOrSpam(this.isTargetOrSpam());
        application.setAdType(this.adType);
        application.setCreative(this.creative);

        return application;
    }
}
