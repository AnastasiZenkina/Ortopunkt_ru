package com.ortopunkt.telegram.integration.social;

import com.ortopunkt.dto.response.CampaignResponseDto;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class TargetReportFormatter {

    public static String format(CampaignResponseDto campaignDto) {
        if (campaignDto == null) {
            return "Нет данных по рекламе за этот период.";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<b>Реклама (VK):</b>\n\n");
        sb.append("• Подписчики: ").append(safeValue(campaignDto.getSubscribers())).append("\n");
        sb.append("• Сообщения: ").append(safeValue(campaignDto.getMessages())).append("\n");
        sb.append("• Цена подписчика: ").append(String.format(Locale.US, "%.2f", campaignDto.getCpa())).append("\n");
        sb.append("• Охват: ").append(safeValue(campaignDto.getReach())).append("\n\n");

        Map<String, Double> creativeStats = campaignDto.getCreativeStats();
        Map<String, Integer> creativeSubs = campaignDto.getCreativeSubs();

        if (creativeStats != null && !creativeStats.isEmpty()) {
            sb.append("<b>Креативы</b>\n\n");
            creativeStats.entrySet().stream()
                    .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                    .limit(10)
                    .forEach(entry -> {
                        String id = entry.getKey();
                        double ctr = entry.getValue();
                        String subs = "0";
                        if (creativeSubs != null && creativeSubs.containsKey(id)) {
                            subs = safeValue(creativeSubs.get(id));
                        }
                        sb.append("ID ").append(id)
                                .append(" — ")
                                .append(String.format(Locale.US, "%.2f", ctr))
                                .append("% (")
                                .append(subs).append(" подписч.)\n");
                    });
        } else {
            sb.append("Нет данных по креативам.\n");
        }

        return sb.toString();
    }

    private static String safeValue(Object value) {
        try {
            if (value == null) return "0";
            long num;
            if (value instanceof Number n) num = n.longValue();
            else {
                String s = value.toString().trim().replaceAll("[^0-9.,-]", "");
                if (s.isEmpty()) return "0";
                num = (long) Double.parseDouble(s.replace(",", "."));
            }
            NumberFormat nf = NumberFormat.getInstance(new Locale("ru"));
            return nf.format(num);
        } catch (Exception e) {
            return "0";
        }
    }
}