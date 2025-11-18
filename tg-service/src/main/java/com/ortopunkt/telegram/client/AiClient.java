package com.ortopunkt.telegram.client;

import com.ortopunkt.dto.request.AiRequest;
import com.ortopunkt.dto.response.AiResponse;
import com.ortopunkt.dto.response.AnalysisResult;
import com.ortopunkt.logging.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AiClient(RestTemplate restTemplate,
                    @Value("${ai.url}") String aiUrl) {
        this.restTemplate = restTemplate;
        this.aiUrl = aiUrl;
    }

    public AiResponse getResponse(String text, boolean hasPhoto) {
        try {
            AiRequest req = new AiRequest();
            req.setText(text);
            req.setHasPhoto(hasPhoto);
            return restTemplate.postForObject(
                    aiUrl + "/api/ai/response",
                    req,
                    AiResponse.class
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return new AiResponse("Спасибо за сообщение! Врач посмотрит и ответит вам лично.");
        }
    }

    public AnalysisResult analyze(String text) {
        try {
            return restTemplate.postForObject(
                    aiUrl + "/api/ai/analyze",
                    text,
                    AnalysisResult.class
            );
        } catch (Exception e) {
            GlobalExceptionHandler.logError(e);
            return new AnalysisResult();
        }
    }
}