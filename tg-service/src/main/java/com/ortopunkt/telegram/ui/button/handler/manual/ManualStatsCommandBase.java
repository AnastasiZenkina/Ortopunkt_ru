package com.ortopunkt.telegram.ui.button.handler.manual;

import com.ortopunkt.dto.request.PostRequestDto;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public abstract class ManualStatsCommandBase {

    private final String commandName;
    private final String platformName;
    private final String apiUrl;
    private final RestTemplate restTemplate;

    protected abstract PostRequestDto parseInput(String text, boolean isInstagram);

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");
    private static final Map<Long, String> waitingMap = new ConcurrentHashMap<>();

    public boolean handle(Update update, AbsSender sender) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return false;
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        if (text.equalsIgnoreCase(commandName)) {
            waitingMap.put(chatId, commandName);
            send(sender, chatId, """
                <b>Введите данные за месяц (в числах):</b>

                Подписчики
                Посещения
                Сообщения
                Охват
                Лайки
                Комментарии
                Репосты

                Только для инстаграм
                Всего (подписок группы)
                Лучшее (ссылка на лучший пост)
                Худшее (ссылка на худший пост)
                """);
            return true;
        }

        String waiting = waitingMap.get(chatId);
        if (!commandName.equals(waiting)) return false;

        if (text.toLowerCase().matches("(?s).*(подпис|посещ|сообщ|охват|лайк|коммент|репост|всего|лучше|худше).*")) {
            try {
                PostRequestDto dto = parseInput(text, platformName.equalsIgnoreCase("INSTAGRAM"));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<PostRequestDto> req = new HttpEntity<>(dto, headers);
                restTemplate.postForObject(apiUrl, req, String.class);
                send(sender, chatId, "Данные сохранены для " + platformName);
                waitingMap.remove(chatId);
                return true;
            } catch (Exception e) {
                log.error("Ошибка при обработке ручных данных: " + e.getMessage());
                send(sender, chatId, "Ошибка: " + e.getMessage());
                waitingMap.remove(chatId);
                return true;
            }
        }

        return false;
    }

    private void send(AbsSender sender, Long chatId, String text) {
        try {
            SendMessage msg = new SendMessage(chatId.toString(), text);
            msg.setParseMode("HTML");
            sender.execute(msg);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }
}