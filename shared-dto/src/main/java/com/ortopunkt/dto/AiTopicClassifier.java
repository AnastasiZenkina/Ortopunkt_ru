package com.ortopunkt.dto;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class AiTopicClassifier {

    private static final String MODEL_URL = "http://localhost:5005/embed";

    private static final Map<String, String> topicDescriptions = Map.ofEntries(
            Map.entry("flatfoot", "плоскостопие, уплощение стопы, стельки, походка"),
            Map.entry("hallux valgus", "шишка, косточка на ноге, большой палец, вальгус"),
            Map.entry("first joint prosthesis", "артродез, эндопротез первого плюснефалангового сустава"),
            Map.entry("ganglion", "гигрома, шишка на кисти, жидкость, рука"),
            Map.entry("dupuytren", "болезнь Дюпюитрена, пальцы, контрактура"),
            Map.entry("endoprosthesis", "тазобедренный сустав, коленный сустав, протез, операция"),
            Map.entry("repeat", "повторная операция, не помогло, было уже вмешательство"),
            Map.entry("symptom", "боль, жжение, отёк, покраснение, симптом, ощущение"),
            Map.entry("morton", "неврома Мортона, стопа, боль между пальцами"),
            Map.entry("haglund", "деформация Хаглунда, ахилл, бугор, пятка"),
            Map.entry("rheumatoid", "ревматоидный артрит, деформация стоп, воспаление"),
            Map.entry("heel spur", "пяточная шпора, фасциит, боль в пятке, шип"),
            Map.entry("quota", "квота, бесплатно, операция по полису"),
            Map.entry("paid", "платно, стоимость, цена операции"),
            Map.entry("region", "город, выезд, где вы находитесь"),
            Map.entry("online", "онлайн-консультация, удалённо, по переписке"),
            Map.entry("rehab", "реабилитация, восстановление, стельки, ортопедия"),
            Map.entry("age", "возраст, старше 60, пожилой, можно ли мне"),
            Map.entry("partner", "реклама, сотрудничество, партнёрство")
    );

    public String classify(String inputText) {
        List<String> textsToEmbed = new ArrayList<>();
        textsToEmbed.add(inputText);
        textsToEmbed.addAll(topicDescriptions.values());

        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("texts", textsToEmbed);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, List<String>>> entity = new HttpEntity<>(requestBody, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List> response = restTemplate.exchange(
                    MODEL_URL,
                    HttpMethod.POST,
                    entity,
                    List.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return "common";
            }

            List<List<Double>> embeddings = (List<List<Double>>) response.getBody();
            List<Double> inputEmbedding = embeddings.get(0);

            double bestSim = -1.0;
            String bestTopic = "common";

            int i = 1;
            for (String topic : topicDescriptions.keySet()) {
                List<Double> topicEmbedding = embeddings.get(i++);
                double sim = cosineSimilarity(inputEmbedding, topicEmbedding);
                if (sim > bestSim) {
                    bestSim = sim;
                    bestTopic = topic;
                }
            }

            return bestTopic;

        } catch (Exception e) {
            e.printStackTrace();
            return "common";
        }
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            double a = v1.get(i);
            double b = v2.get(i);
            dot += a * b;
            norm1 += a * a;
            norm2 += b * b;
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }
}
