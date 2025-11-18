package com.ortopunkt.telegram.integration.social;

import com.ortopunkt.dto.response.AnalysisResult;
import com.ortopunkt.dto.response.ApplicationResponseDto;
import com.ortopunkt.logging.GlobalExceptionHandler;
import com.ortopunkt.telegram.client.AiClient;
import com.ortopunkt.telegram.client.CrmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiClient aiClient;
    private final CrmClient crmClient;

    public AnalysisResult analyzeApplication(Long appId) {
        try {
            ApplicationResponseDto app = crmClient.getApplication(appId);
            if (app == null || app.getText() == null) return null;
            return aiClient.analyze(app.getText());
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return null;
        }
    }
}