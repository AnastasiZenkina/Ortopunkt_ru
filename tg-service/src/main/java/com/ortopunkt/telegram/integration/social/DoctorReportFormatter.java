package com.ortopunkt.telegram.integration.social;

import com.ortopunkt.dto.response.PostResponseDto;
import com.ortopunkt.dto.response.CampaignResponseDto;

import java.text.NumberFormat;
import java.util.Locale;

public class DoctorReportFormatter {

    private static String trimTitle(String title, int maxLen) {
        if (title == null) return "";
        return title.length() > maxLen
                ? title.substring(0, maxLen).trim() + "..."
                : title.trim();
    }

    public static String format(PostResponseDto vkDto,
                                PostResponseDto instaDto,
                                CampaignResponseDto campaignDto) {

        StringBuilder sb = new StringBuilder();
        String monthYear = vkDto != null ? vkDto.getMonthYear() : null;

        if (monthYear != null) {
            sb.append("\uD83D\uDCCC <b>Отчёт за ").append(monthYear).append("</b>\n\n");
        }

        if (vkDto != null) {
            long total = vkDto.getVkSubscribers() != null ? vkDto.getVkSubscribers() : 0;
            long ads = (campaignDto != null && campaignDto.getSubscribers() != null) ? campaignDto.getSubscribers() : 0;
            long organic = Math.max(0, total - ads);
            long vkMessages = vkDto.getVkMessages() != null ? vkDto.getVkMessages() : 0;
            long adsMessages = (campaignDto != null && campaignDto.getMessages() != null) ? campaignDto.getMessages() : 0;
            long vkReach = vkDto.getVkReach() != null ? vkDto.getVkReach() : 0;
            long adsReach = (campaignDto != null && campaignDto.getReach() != null) ? campaignDto.getReach() : 0;
            double cpa = (campaignDto != null && campaignDto.getCpa() != null) ? campaignDto.getCpa() : 0.0;

            sb.append("<b>Сообщество VK — ")
                    .append(safeValue(vkDto.getVkMembersTotal()))
                    .append(" подписчиков (в группе)</b>\n\n");

            sb.append("• Подписчики: ").append(safeValue(total)).append("\n");
            sb.append("  → реклама: ").append(safeValue(ads)).append("\n");
            sb.append("  → смм: ").append(safeValue(organic)).append("\n");
            sb.append("  → цена подписчика: ").append(String.format(Locale.US, "%.2f", cpa)).append("₽\n\n");

            sb.append("• Сообщения: ").append(safeValue(vkMessages)).append("\n");
            sb.append("  → реклама: ").append(safeValue(adsMessages)).append("\n\n");

            sb.append("• Комментарии: ").append(safeValue(vkDto.getVkComments())).append("\n");
            sb.append("• Охват: ").append(safeValue(vkReach)).append("\n");
            sb.append("  → реклама: ").append(safeValue(adsReach)).append("\n\n");

            sb.append("• Лучший пост: ");
            if (vkDto.getBestPostUrl() != null && !vkDto.getBestPostUrl().isBlank()) {
                sb.append("<a href=\"").append(vkDto.getBestPostUrl()).append("\">")
                        .append(trimTitle(vkDto.getBestPostTitle(), 40))
                        .append("</a>\n\n");
            } else sb.append("— нет данных\n\n");
        }

        if (instaDto != null) {
            sb.append("<b>Сообщество Instagram — ")
                    .append(safeValue(instaDto.getInstaFollowersTotal()))
                    .append(" подписчиков (в группе)</b>\n\n");

            sb.append("• Подписчики: ").append(safeValue(instaDto.getInstaSubscribers())).append("\n");
            sb.append("• Сообщения: ").append(safeValue(instaDto.getInstaMessages())).append("\n");
            sb.append("• Комментарии: ").append(safeValue(instaDto.getInstaComments())).append("\n");
            sb.append("• Охват контента: ").append(safeValue(instaDto.getInstaReach())).append("\n");

            sb.append("• Лучший пост: ");
            if (instaDto.getInstaBestPostUrl() != null && !instaDto.getInstaBestPostUrl().isBlank()) {
                sb.append("<a href=\"").append(instaDto.getInstaBestPostUrl()).append("\">ссылка</a>\n\n");
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