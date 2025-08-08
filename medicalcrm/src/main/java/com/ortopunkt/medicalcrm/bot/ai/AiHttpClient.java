package com.ortopunkt.medicalcrm.bot.ai;

import com.ortopunkt.dto.AiRequest;
import com.ortopunkt.dto.AiResponse;
import com.ortopunkt.dto.AnalysisResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiHttpClient {

    private final RestTemplate restTemplate;
    private final String aiUrl; // ожидаю, что это что-то вроде http://localhost:8081/api/ai

    public AiHttpClient(@Value("${ai.url}") String aiUrl) {
        this.restTemplate = new RestTemplate();
        this.aiUrl = aiUrl;
    }

    public AiResponse getResponse(String text, boolean hasPhoto) {
        try {
            AiRequest req = new AiRequest();
            req.setText(text);
            req.setHasPhoto(hasPhoto);

            return restTemplate.postForObject(
                    aiUrl + "/response",  // => /api/ai/response
                    req,
                    AiResponse.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new AiResponse("Спасибо за сообщение! Врач посмотрит и ответит вам лично 🌿");
        }
    }

    public AnalysisResult analyze(String text) {
        try {
            return restTemplate.postForObject(
                    aiUrl + "/analyze",   // => /api/ai/analyze
                    text,
                    AnalysisResult.class
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}