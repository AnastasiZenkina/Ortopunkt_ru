package com.ortopunkt.ai.controller;

import com.ortopunkt.ai.analysis.AiAnalyzer;
import com.ortopunkt.dto.response.AnalysisResult;
import com.ortopunkt.dto.request.AiRequest;
import com.ortopunkt.dto.response.AiResponse;
import com.ortopunkt.ai.topic.AiTopicClassifier;
import com.ortopunkt.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService service;
    private final AiTopicClassifier classifier;
    private final AiAnalyzer analyzer;

    @PostMapping("/response")
    public AiResponse getResponse(@RequestBody AiRequest request) {
        return service.getResponse(request.getText(), request.isHasPhoto());
    }

    @PostMapping("/classify")
    public String classify(@RequestBody String text) {
        return classifier.classify(text);
    }

    @PostMapping("/analyze")
    public AnalysisResult analyze(@RequestBody String text) {
        return analyzer.analyze(text);
    }
}
