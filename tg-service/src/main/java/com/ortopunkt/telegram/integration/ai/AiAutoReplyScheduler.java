package com.ortopunkt.telegram.integration.ai;

import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.telegram.client.CrmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAutoReplyScheduler {

    private final CrmClient crmClient;
    private final AiAutoReplySender autoReplySender;

    @Scheduled(fixedRate = 30000)
    public void checkAndSendAutoReplies() {
        List<ApplicationResponseDto> all = crmClient.getAllApplications();

        for (ApplicationResponseDto app : all) {
            if (app == null) continue;
            if (app.isAnsweredByHuman()) continue;
            if (app.isAnsweredByAi()) continue;
            if (app.getPhotoFileIds() != null && !app.getPhotoFileIds().isEmpty()) continue;

            LocalDate created = app.getCreatedAt();
            if (created == null) continue;
            if (created.isBefore(LocalDate.now().minusDays(2))) continue;

            if (app.getStatus() == null || "NEW".equalsIgnoreCase(app.getStatus())) {
                autoReplySender.send(app);
            }
        }
    }
}