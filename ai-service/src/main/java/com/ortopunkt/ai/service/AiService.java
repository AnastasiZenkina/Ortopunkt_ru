package com.ortopunkt.ai.service;

import com.ortopunkt.ai.topic.AiTopicClassifier;
import com.ortopunkt.ai.topic.ReplyTemplates;
import com.ortopunkt.dto.response.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiTopicClassifier classifier;
    private final ReplyTemplates templates;

    public AiResponse getResponse(String text, boolean hasPhoto) {
        String topic = classifier.classify(text);
        String reply;

        switch (topic) {
            case "flatfoot": reply = templates.flatfoot(hasPhoto); break;
            case "hallux valgus": reply = templates.halluxValgus(hasPhoto); break;
            case "first joint prosthesis": reply = templates.firstJointProsthesis(hasPhoto); break;
            case "ganglion": reply = templates.ganglion(hasPhoto); break;
            case "dupuytren": reply = templates.dupuytren(hasPhoto); break;
            case "endoprosthesis": reply = templates.endoprosthesis(hasPhoto); break;
            case "repeat": reply = templates.repeat(hasPhoto); break;
            case "symptom": reply = templates.symptom(hasPhoto); break;
            case "morton": reply = templates.morton(hasPhoto); break;
            case "haglund": reply = templates.haglund(hasPhoto); break;
            case "rheumatoid": reply = templates.rheumatoid(hasPhoto); break;
            case "heel spur": reply = templates.heelSpur(hasPhoto); break;
            case "quota": reply = templates.quota(); break;
            case "paid": reply = templates.paid(); break;
            case "region": reply = templates.region(); break;
            case "online": reply = templates.online(); break;
            case "rehab": reply = templates.rehab(); break;
            case "age": reply = templates.age(); break;
            case "partner": reply = templates.partner(); break;
            default: reply = templates.common(); break;
        }

        return new AiResponse(reply);
    }
}
