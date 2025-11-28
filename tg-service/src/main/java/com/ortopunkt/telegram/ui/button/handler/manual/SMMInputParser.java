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

    private static final Pattern P = Pattern.compile(
            "(?iu)(" +
                    "подпис\\w*|" +
                    "подписчик\\w*|" +
                    "подписчики|" +
                    "посещ\\w*|" +
                    "посещения|" +
                    "сообщ\\w*|" +
                    "сообщения|" +
                    "охват\\w*|" +
                    "лайк\\w*|" +
                    "лайки|" +
                    "лайков|" +
                    "коммент\\w*|" +
                    "комментар\\w*|" +
                    "комментарии|" +
                    "комменты|" +
                    "репост\\w*|" +
                    "репосты|" +
                    "репостов|" +
                    "всего" +
                    ")\\s*([0-9.,kк\\s\\+]+)(?=\\n|$)"
    );

    public PostRequestDto parseInput(String text, boolean isInstagram) {
        PostRequestDto dto = new PostRequestDto();
        if (text == null || text.isBlank()) return dto;

        try {
            log.info("Получен текст для парсинга: " + text);

            Matcher matcher = P.matcher(text);

            while (matcher.find()) {

                String key = matcher.group(1).toLowerCase(Locale.ROOT);
                String nums = matcher.group(2);

                double sum = 0;

                for (String part : nums.split("\\+")) {
                    String n = part.trim();
                    if (n.isEmpty()) continue;

                    boolean hasK = n.toLowerCase(Locale.ROOT).contains("k")
                            || n.toLowerCase(Locale.ROOT).contains("к");

                    String digits = n.replaceAll("[^0-9.,]", "");
                    if (digits.isEmpty()) continue;

                    digits = digits.replace(",", ".");
                    double v = Double.parseDouble(digits);

                    if (hasK) v *= 1000;

                    sum += v;
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

                } else if (key.contains("коммент") || key.contains("комментар")) {
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
            log.error("Ошибка при разборе текста: " + e.getMessage());
        }

        return dto;
    }

    public PostRequestDto parseInput(String text) {
        return parseInput(text, false);
    }
}