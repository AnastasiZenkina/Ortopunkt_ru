package com.example.medicalcrm.bot.button;

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
    //private final AiCommand aiCommand;
    private final MenuCommand menuCommand;

    @Bean
    public Map<String, ButtonCommand> commandMap() {
        Map<String, ButtonCommand> map = new HashMap<>();

        map.put("MARK_", markCommand);
        map.put("PAID_", paidCommand);
        map.put("FREE_", freeCommand);
     //   map.put("AI_", aiCommand);
        map.put("DOCTOR_REPORT", menuCommand);
        map.put("DOCTOR_PATIENTS", menuCommand);
        map.put("SMM_REPORT", menuCommand);
        map.put("TARGET_REPORT", menuCommand);

        return map;
    }
}
