package com.ortopunkt.ai.analysis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.ortopunkt.dto.response.AnalysisResult;
import org.springframework.http.*;
import java.util.*;

@Component
@Slf4j
public class AiAnalyzer {

    private static final String MODEL_URL = "http://localhost:5005/nli"; // эндпоинт второго ИИ

    private static final Map<String, String> HYPOTHESES = Map.ofEntries(
            // Интерес
            Map.entry("interest", "Пациент заинтересован в лечении"),

            // Платёжеспособность
            Map.entry("wants_paid", "Пациент хочет лечиться платно"),
            Map.entry("wants_free", "Пациент хочет лечиться бесплатно"),
            Map.entry("mentions_no_money", "Пациент пишет, что у него нет денег"),
            Map.entry("asks_about_discount", "Пациент интересуется скидками или акциями"),

            // Эмоции
            Map.entry("anxious", "Пациент тревожится"),
            Map.entry("aggressive", "Пациент злится или раздражён"),

            // Готовность
            Map.entry("ready_for_surgery", "Пациент хочет оперироваться как можно скорее"),
            Map.entry("wants_consultation", "Пациент хочет только консультацию"),
            Map.entry("gathering_info", "Пациент пока просто узнаёт информацию"),
            Map.entry("not_ready", "Пациент не готов к лечению сейчас")
    );

    public AnalysisResult analyze(String text) {
        try {
            List<String> hypotheses = new ArrayList<>(HYPOTHESES.values());

            Map<String, Object> requestBody = Map.of(
                    "premise", text,
                    "hypotheses", hypotheses
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    MODEL_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("Model response was null or not 200");
                return fallback();
            }

            Map<String, String> results = (Map<String, String>) response.getBody();
            return interpret(text, results);

        } catch (Exception e) {
            log.error("Ошибка AI анализа", e);
            return fallback();
        }
    }

    private AnalysisResult fallback() {
        return new AnalysisResult("неясен", "неясна", "неопределены", "неясна", "передать врачу для ручного разбора");
    }

    private AnalysisResult interpret(String text, Map<String, String> result) {
        // Интерес
        String interest = result.getOrDefault("Пациент заинтересован в лечении", "no").equals("yes") ? "высокий" : "низкий";

        // Платёжеспособность
        String payment = "неясна";
        if ("yes".equals(result.getOrDefault("Пациент хочет лечиться платно", "no"))) {
            payment = "вероятна";
        } else if ("yes".equals(result.getOrDefault("Пациент хочет лечиться бесплатно", "no"))
                || "yes".equals(result.getOrDefault("Пациент пишет, что у него нет денег", "no"))) {
            payment = "маловероятна";
        } else if ("yes".equals(result.getOrDefault("Пациент интересуется скидками или акциями", "no"))) {
            payment = "средняя (уточняет про акции)";
        }

        // Эмоции
        String emotion = "спокойный";
        if ("yes".equals(result.getOrDefault("Пациент тревожится", "no"))) {
            emotion = "тревожный";
        } else if ("yes".equals(result.getOrDefault("Пациент злится или раздражён", "no"))) {
            emotion = "раздражённый";
        }

        // Готовность
        String readiness = "неясна";
        if ("yes".equals(result.getOrDefault("Пациент хочет оперироваться как можно скорее", "no"))) {
            readiness = "готов к операции";
        } else if ("yes".equals(result.
                getOrDefault("Пациент хочет только консультацию", "no"))) {
            readiness = "хочет консультацию";
        } else if ("yes".equals(result.getOrDefault("Пациент пока просто узнаёт информацию", "no"))) {
            readiness = "собирает информацию";
        } else if ("yes".equals(result.getOrDefault("Пациент не готов к лечению сейчас", "no"))) {
            readiness = "не готов";

        }

        // Рекомендация
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

