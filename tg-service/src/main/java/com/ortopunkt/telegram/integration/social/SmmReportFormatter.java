package com.ortopunkt.telegram.integration.social;

import com.ortopunkt.dto.response.PostResponseDto;

import java.text.NumberFormat;
import java.util.Locale;

public class SmmReportFormatter {

    private static String trimTitle(String title, int maxLen) {
        if (title == null) return "";
        return title.length() > maxLen
                ? title.substring(0, maxLen).trim() + "..."
                : title.trim();
    }

    public static String format(PostResponseDto vkDto, PostResponseDto instaDto) {
        StringBuilder sb = new StringBuilder();
        String monthYear = vkDto != null ? vkDto.getMonthYear() : null;

        if (monthYear != null) {
            sb.append("<b>Отчёт за ").append(monthYear).append("</b>\n\n");
        }

        if (vkDto != null) {
            sb.append("<b>Сообщество VK — ")
                    .append(safeValue(vkDto.getVkMembersTotal()))
                    .append(" подписчиков (всего)</b>\n\n");

            sb.append("• Подписчики: ").append(safeValue(vkDto.getVkSubscribers())).append("\n");
            sb.append("• Посещения: ").append(safeValue(vkDto.getVkVisitors())).append("\n");
            sb.append("• Сообщения: ").append(safeValue(vkDto.getVkMessages())).append("\n");
            sb.append("• Охват контента: ").append(safeValue(vkDto.getVkReach())).append("\n\n");

            sb.append("• Лайки: ").append(safeValue(vkDto.getVkLikes())).append("\n");
            sb.append("• Комментарии: ").append(safeValue(vkDto.getVkComments())).append("\n");
            sb.append("• Репосты: ").append(safeValue(vkDto.getVkShares())).append("\n");

            sb.append("• Лучший пост: ");
            if (vkDto.getBestPostUrl() != null && !vkDto.getBestPostUrl().isBlank()) {
                sb.append("<a href=\"").append(vkDto.getBestPostUrl()).append("\">")
                        .append(trimTitle(vkDto.getBestPostTitle(), 40))
                        .append("</a>\n");
            } else sb.append("— нет данных\n");

            sb.append("• Слабый пост: ");
            if (vkDto.getWorstPostUrl() != null && !vkDto.getWorstPostUrl().isBlank()) {
                sb.append("<a href=\"").append(vkDto.getWorstPostUrl()).append("\">")
                        .append(trimTitle(vkDto.getWorstPostTitle(), 40))
                        .append("</a>\n\n");
            } else sb.append("— нет данных\n\n");
        }

        if (instaDto != null) {
            sb.append("<b>Сообщество Instagram — ")
                    .append(safeValue(instaDto.getInstaFollowersTotal()))
                    .append(" подписчиков (всего)</b>\n\n");

            sb.append("• Подписчики: ").append(safeValue(instaDto.getInstaSubscribers())).append("\n");
            sb.append("• Посещения: ").append(safeValue(instaDto.getInstaVisitors())).append("\n");
            sb.append("• Сообщения: ").append(safeValue(instaDto.getInstaMessages())).append("\n");
            sb.append("• Охват контента: ").append(safeValue(instaDto.getInstaReach())).append("\n\n");

            sb.append("• Лайки: ").append(safeValue(instaDto.getInstaLikes())).append("\n");
            sb.append("• Комментарии: ").append(safeValue(instaDto.getInstaComments())).append("\n");
            sb.append("• Репосты: ").append(safeValue(instaDto.getInstaShares())).append("\n");

            sb.append("• Лучший пост: ");
            if (instaDto.getInstaBestPostUrl() != null && !instaDto.getInstaBestPostUrl().isBlank()) {
                sb.append("<a href=\"").append(instaDto.getInstaBestPostUrl()).append("\">ссылка</a>\n");
            } else sb.append("— нет данных\n");

            sb.append("• Слабый пост: ");
            if (instaDto.getInstaWorstPostUrl() != null && !instaDto.getInstaWorstPostUrl().isBlank()) {
                sb.append("<a href=\"").append(instaDto.getInstaWorstPostUrl()).append("\">ссылка</a>\n\n");
            } else sb.append("— нет данных\n\n");
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