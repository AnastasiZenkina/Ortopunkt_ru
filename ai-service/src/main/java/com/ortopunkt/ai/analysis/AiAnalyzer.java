package com.ortopunkt.ai.analysis;

import com.ortopunkt.ai.config.AiDictionaries;
import com.ortopunkt.dto.response.AnalysisResult;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AiAnalyzer {

    private final RestTemplate restTemplate;
    private final ServiceLogger log = new ServiceLogger(getClass(), "AI");

    @Value("${ai.model.nli.url}")
    private String modelUrl;

    public AnalysisResult analyze(String text) {
        try {
            List<String> hypotheses = new ArrayList<>(AiDictionaries.HYPOTHESES.values());
            Map<String, Object> requestBody = Map.of("premise", text, "hypotheses", hypotheses);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(modelUrl, HttpMethod.POST, entity, Map.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("Model response was null or not 200");
                return fallback();
            }
            Map<String, String> results = (Map<String, String>) response.getBody();
            return interpret(results);
        } catch (Exception e) {
            log.error("Ошибка AI анализа: " + e.getMessage());
            return fallback();
        }
    }

    private AnalysisResult fallback() {
        return new AnalysisResult("неясен", "неясна", "неопределены", "неясна", "передать врачу для ручного разбора");
    }

    private AnalysisResult interpret(Map<String, String> result) {
        String interest = "yes".equals(result.getOrDefault("Пациент заинтересован в лечении", "no")) ? "высокий" : "низкий";
        String payment;
        if ("yes".equals(result.getOrDefault("Пациент хочет лечиться платно", "no"))) {
            payment = "вероятна";
        } else if ("yes".equals(result.getOrDefault("Пациент хочет лечиться бесплатно", "no"))
                || "yes".equals(result.getOrDefault("Пациент пишет, что у него нет денег", "no"))) {
            payment = "маловероятна";
        } else if ("yes".equals(result.getOrDefault("Пациент интересуется скидками или акциями", "no"))) {
            payment = "средняя (уточняет про акции)";
        } else {
            payment = "неясна";
        }
        String emotion = "yes".equals(result.getOrDefault("Пациент тревожится", "no")) ? "тревожный"
                : "yes".equals(result.getOrDefault("Пациент злится или раздражён", "no")) ? "раздражённый" : "спокойный";
        String readiness = "неясна";
        if ("yes".equals(result.getOrDefault("Пациент хочет оперироваться как можно скорее", "no"))) {
            readiness = "готов к операции";
        } else if ("yes".equals(result.getOrDefault("Пациент хочет только консультацию", "no"))) {
            readiness = "хочет консультацию";
        } else if ("yes".equals(result.getOrDefault("Пациент пока просто узнаёт информацию", "no"))) {
            readiness = "собирает информацию";
        } else if ("yes".equals(result.getOrDefault("Пациент не готов к лечению сейчас", "no"))) {
            readiness = "не готов";
        }
        String recommendation = switch (readiness) {
            case "готов к операции" -> "записать на приём";
            case "хочет консультацию" -> "дать информацию и предложить записаться";
            case "не готов" -> "ответить мягко, не настаивать";
            case "собирает информацию" -> "ответить с поддержкой, дать варианты";
            default -> "передать врачу для ручного разбора";
        };
        return new AnalysisResult(interest, payment, emotion, readiness, recommendation);
    }
}