package com.ortopunkt.ai.service;

import com.ortopunkt.ai.topic.Topic;
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
        Topic topic = classifier.classify(text);
        String reply;

        switch (topic) {
            case FLATFOOT -> reply = templates.flatfoot(hasPhoto);
            case HALLUX_VALGUS -> reply = templates.halluxValgus(hasPhoto);
            case FIRST_JOINT_PROSTHESIS -> reply = templates.firstJointProsthesis(hasPhoto);
            case GANGLION -> reply = templates.ganglion(hasPhoto);
            case DUPUYTREN -> reply = templates.dupuytren(hasPhoto);
            case ENDOPROSTHESIS -> reply = templates.endoprosthesis(hasPhoto);
            case REPEAT -> reply = templates.repeat(hasPhoto);
            case SYMPTOM -> reply = templates.symptom(hasPhoto);
            case MORTON -> reply = templates.morton(hasPhoto);
            case HAGLUND -> reply = templates.haglund(hasPhoto);
            case RHEUMATOID -> reply = templates.rheumatoid(hasPhoto);
            case HEEL_SPUR -> reply = templates.heelSpur(hasPhoto);
            case QUOTA -> reply = templates.quota();
            case PAID -> reply = templates.paid();
            case REGION -> reply = templates.region();
            case ONLINE -> reply = templates.online();
            case REHAB -> reply = templates.rehab();
            case AGE -> reply = templates.age();
            case PARTNER -> reply = templates.partner();
            default -> reply = templates.common();
        }

        return new AiResponse(reply);
    }
}