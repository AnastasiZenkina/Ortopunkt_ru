package com.ortopunkt.ai.topic;

import com.ortopunkt.ai.config.AiDictionaries;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AiTopicClassifier {

    @Value("${ai.model.url}")
    private String modelUrl;

    private static final double MIN_SIM = 0.30;

    private final RestTemplate restTemplate;
    private final ServiceLogger log = new ServiceLogger(getClass(), "AI");

    public Topic classify(String inputText) {
        String text = inputText == null ? "" : inputText.toLowerCase();

        Topic kwHit = keywordFallback(text);
        if (kwHit != Topic.COMMON) return kwHit;

        List<Topic> topics = new ArrayList<>(AiDictionaries.TOPIC_DESCRIPTIONS.keySet());
        List<String> textsToEmbed = new ArrayList<>();
        textsToEmbed.add(text);
        textsToEmbed.addAll(AiDictionaries.TOPIC_DESCRIPTIONS.values());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(Map.of("texts", textsToEmbed), headers);

        try {
            ResponseEntity<List<List<Double>>> resp = restTemplate.exchange(
                    modelUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {}
            );
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) return kwHit;

            List<List<Double>> embs = resp.getBody();
            List<Double> in = embs.get(0);

            Topic best = Topic.COMMON;
            double bestScore = -1;
            for (int i = 0; i < topics.size(); i++) {
                double s = cosineSimilarity(in, embs.get(i + 1));
                if (s > bestScore) {
                    bestScore = s;
                    best = topics.get(i);
                }
            }
            if (bestScore >= MIN_SIM) return best;

            return keywordFallback(text);
        } catch (Exception e) {
            log.error("Error during topic classification: " + e.getMessage());
            return keywordFallback(text);
        }
    }

    private Topic keywordFallback(String textLower) {
        Map<Topic, List<String>> kw = buildKeywords();
        for (Topic t : AiDictionaries.TOPIC_DESCRIPTIONS.keySet()) {
            for (String k : kw.getOrDefault(t, List.of())) {
                if (!k.isEmpty() && textLower.contains(k)) return t;
            }
        }
        return Topic.COMMON;
    }

    private Map<Topic, List<String>> buildKeywords() {
        Map<Topic, String> dict = AiDictionaries.TOPIC_DESCRIPTIONS;
        Map<Topic, List<String>> out = new LinkedHashMap<>();
        for (Map.Entry<Topic, String> e : dict.entrySet()) {
            List<String> list = Stream.of(e.getValue().toLowerCase().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            out.put(e.getKey(), list);
        }
        return out;
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dot = 0.0, n1 = 0.0, n2 = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            double a = v1.get(i), b = v2.get(i);
            dot += a * b; n1 += a * a; n2 += b * b;
        }
        return dot / (Math.sqrt(n1) * Math.sqrt(n2) + 1e-10);
    }
}