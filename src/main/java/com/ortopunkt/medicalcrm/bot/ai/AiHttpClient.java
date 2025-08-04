package com.ortopunkt.medicalcrm.bot.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiHttpClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AiHttpClient(@Value("${ai.url}") String aiUrl) {
        this.restTemplate = new RestTemplate();
        this.aiUrl = aiUrl;
    }

    public AiResponse getResponse(String text, boolean hasPhoto) {
        try {
            AiRequest req = new AiRequest(text, hasPhoto, false);
            return restTemplate.postForObject(aiUrl + "/response", req, AiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new AiResponse("Спасибо за сообщение! Врач посмотрит и ответит вам лично 🌿");
        }
    }

    public AnalysisResult analyze(String text) {
        try {
            return restTemplate.postForObject(aiUrl + "/analyze", text, AnalysisResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
