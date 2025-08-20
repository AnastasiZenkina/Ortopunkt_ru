package com.ortopunkt.telegram.ui.button;

import com.ortopunkt.telegram.ui.button.handler.AiCommand;
import com.ortopunkt.telegram.ui.button.handler.AnswerCommand;
import com.ortopunkt.telegram.ui.button.handler.MarkCommand;
import com.ortopunkt.telegram.ui.button.handler.MenuCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ButtonCommandConfig {

    private final MarkCommand markCommand;
    private final AiCommand aiCommand;
    private final MenuCommand menuCommand;
    private final AnswerCommand answerCommand;

    @Bean("buttonCommandMap")
    public Map<String, ButtonCommand> buttonCommandMap() {
        Map<String, ButtonCommand> map = new HashMap<>();
        // карточка заявки
        map.put("MARK_",           markCommand);
        map.put("ANSWER_",         answerCommand);

        // AI
        map.put("AI_ANALYZE_",     aiCommand);
        map.put("AI_BUTTON",       aiCommand);

        // меню / отчёты
        map.put("DOCTOR_REPORT",   menuCommand);
        map.put("DOCTOR_PATIENTS", menuCommand);
        map.put("SMM_REPORT",      menuCommand);
        map.put("TARGET_REPORT",   menuCommand);

        return map;
    }
}
