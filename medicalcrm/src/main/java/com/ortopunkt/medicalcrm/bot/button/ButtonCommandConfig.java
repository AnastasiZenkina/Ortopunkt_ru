package com.ortopunkt.medicalcrm.bot.button;

import com.ortopunkt.medicalcrm.bot.ai.AiCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ButtonCommandConfig {

    private final MarkCommand markCommand;
    private final PaidCommand paidCommand;
    private final FreeCommand freeCommand;
    private final AiCommand aiCommand;
    private final MenuCommand menuCommand;
    private final AnswerCommand answerCommand;

    @Bean("buttonCommandMap") // имя бина фиксированное
    public Map<String, ButtonCommand> buttonCommandMap() {
        Map<String, ButtonCommand> map = new HashMap<>();
        map.put("MARK_",           markCommand);
        map.put("PAID_",           paidCommand);
        map.put("FREE_",           freeCommand);
        map.put("AI_ANALYZE_",     aiCommand);
        map.put("AI_BUTTON",       aiCommand);
        map.put("DOCTOR_REPORT",   menuCommand);
        map.put("DOCTOR_PATIENTS", menuCommand);
        map.put("SMM_REPORT",      menuCommand);
        map.put("TARGET_REPORT",   menuCommand);
        map.put("ANSWER_",         answerCommand); // ВАЖНО: с подчёркиванием
        return map;
    }
}
