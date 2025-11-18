package com.ortopunkt.telegram.ui.button;

import com.ortopunkt.telegram.ui.button.handler.auto.*;
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
    private final OperatedCommand operatedCommand;
    private final StatusCommand statusCommand;
    private final RoleChangeCommand roleChangeCommand;

    @Bean("buttonCommandMap")
    public Map<String, ButtonCommand> buttonCommandMap() {
        Map<String, ButtonCommand> map = new HashMap<>();

        map.put("MARK_", markCommand);
        map.put("ANSWER_", answerCommand);
        map.put("OPERATED_PAID_", operatedCommand);
        map.put("OPERATED_QUOTA_", operatedCommand);
        map.put("STATUS_", statusCommand);
        map.put("AI_ANALYZE_", aiCommand);
        map.put("AI_BUTTON", aiCommand);
        map.put("DOCTOR_REPORT", menuCommand);
        map.put("DOCTOR_PATIENTS", menuCommand);
        map.put("SMM_REPORT", menuCommand);
        map.put("TARGET_REPORT", menuCommand);
        map.put("CHANGE_ROLE", roleChangeCommand);
        map.put("ROLE_DOCTOR", roleChangeCommand);
        map.put("ROLE_SMM", roleChangeCommand);
        map.put("ROLE_TARGET", roleChangeCommand);

        return map;
    }
}