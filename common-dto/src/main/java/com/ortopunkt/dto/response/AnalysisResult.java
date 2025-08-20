package com.ortopunkt.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {

    private String interest;
    private String paymentAbility;
    private String emotion;
    private String readiness;
    private String recommendation;

    public String toTelegramMessage(){
        return String.format(
                "🤖 ИИ-анализ:\n" +
                        "• Интерес: %s\n" +
                        "• Платёжеспособность: %s\n" +
                        "• Эмоции: %s\n" +
                        "• Готовность: %s\n" +
                        "• Рекомендация: %s",
                interest, paymentAbility, emotion, readiness, recommendation
        );
    }
}
