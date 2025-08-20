package com.ortopunkt.telegram.aiintegration;

import com.ortopunkt.crm.service.ApplicationService;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAutoReplyScheduler {

    private final ApplicationService applicationService;
    private final AiAutoReplySender autoReplySender;

    @Scheduled(fixedRate = 60000) // каждые 5 минут
    public void checkAndSendAutoReplies() {
        List<ApplicationResponseDto> all = applicationService.getAllApplications();

        for (ApplicationResponseDto app : all) {
            if (app.isAnsweredByHuman()) continue;
            if (app.isAnsweredByAi()) continue;
            LocalDate created = app.getCreatedAt();
            if (created != null && created.atStartOfDay().isBefore(LocalDateTime.now().minusMinutes(1))) {
                autoReplySender.send(app);
            }
        }
    }
}
