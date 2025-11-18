package com.ortopunkt.telegram.ui.button.handler.manual;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.logging.ServiceLogger;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SMMInputParser {

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

    public PostRequestDto parseInput(String text, boolean isInstagram) {
        PostRequestDto dto = new PostRequestDto();
        if (text == null || text.isBlank()) return dto;

        try {
            log.info("Получен текст для парсинга: " + text);

            Matcher matcher = Pattern.compile(
                    "(?iu)(подпис[^\\s]*|посещ[^\\s]*|сообщ[^\\s]*|охват[^\\s]*|лайк[^\\s]*|коммент[^\\s]*|репост[^\\s]*|всего)\\s*([\\d+\\s.,kк]+)"
            ).matcher(text);

            while (matcher.find()) {
                String key = matcher.group(1).toLowerCase(Locale.ROOT);
                String nums = matcher.group(2).replace(",", ".").replaceAll("\\s+", "");
                double sum = 0;
                for (String n : nums.split("\\+")) {
                    if (n.isBlank()) continue;
                    double val = Double.parseDouble(n.replaceAll("[^0-9.]", ""));
                    if (n.toLowerCase().contains("k") || n.toLowerCase().contains("к")) val *= 1000;
                    sum += val;
                }
                long val = Math.round(sum);

                if (key.contains("подпис")) {
                    if (isInstagram) dto.setInstaSubscribers((int) val);
                    else dto.setVkSubscribers((int) val);
                } else if (key.contains("посещ")) {
                    if (isInstagram) dto.setInstaVisitors((int) val);
                    else dto.setVkVisitors((int) val);
                } else if (key.contains("сообщ")) {
                    if (isInstagram) dto.setInstaMessages((int) val);
                    else dto.setVkMessages((int) val);
                } else if (key.contains("охват")) {
                    if (isInstagram) dto.setInstaReach((int) val);
                    else dto.setVkReach((int) val);
                } else if (key.contains("лайк")) {
                    if (isInstagram) dto.setInstaLikes((int) val);
                    else dto.setVkLikes((int) val);
                } else if (key.contains("коммент")) {
                    if (isInstagram) dto.setInstaComments((int) val);
                    else dto.setVkComments((int) val);
                } else if (key.contains("репост")) {
                    if (isInstagram) dto.setInstaShares((int) val);
                    else dto.setVkShares((int) val);
                } else if (isInstagram && key.contains("всего")) {
                    dto.setInstaFollowersTotal(val);
                }
            }

            log.info("Результат парсинга DTO: " + dto);
        } catch (Exception e) {
            log.error("Ошибка при разборе текста для SMMInputParser: " + e.getMessage());
        }

        return dto;
    }

    public PostRequestDto parseInput(String text) {
        return parseInput(text, false);
    }
}