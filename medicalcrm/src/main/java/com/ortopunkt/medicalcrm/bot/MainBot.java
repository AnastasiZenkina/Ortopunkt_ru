package com.ortopunkt.medicalcrm.bot;

import com.ortopunkt.medicalcrm.bot.button.ButtonCommand;
import com.ortopunkt.medicalcrm.bot.screen.DoctorScreen;
import com.ortopunkt.medicalcrm.bot.screen.PatientScreen;
import com.ortopunkt.medicalcrm.bot.screen.SmmScreen;
import com.ortopunkt.medicalcrm.bot.screen.TargetScreen;
import com.ortopunkt.medicalcrm.config.BotConfig;
import com.ortopunkt.medicalcrm.entity.BotUser;
import com.ortopunkt.medicalcrm.service.ApplicationService;
import com.ortopunkt.medicalcrm.service.BotUserService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

@Component("mainBot")
@RequiredArgsConstructor
@Getter
public class MainBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final BotUserService botUserService;
    private final DoctorScreen doctorCommandHandler;
    private final SmmScreen smmCommandHandler;
    private final TargetScreen targetCommandHandler;
    private final PatientScreen patientCommandHandler;
    private final ApplicationService applicationService;
    @Resource(name = "buttonCommandMap")
    private Map<String, ButtonCommand> commandMap;

    private static MainBot instance;

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        instance = this;

        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
            return;
        }

        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            botUserService.getBotUserByTelegramId(chatId).orElseGet(() -> {
                BotUser user = new BotUser();
                user.setTelegramId(chatId);
                user.setUsername(username);
                user.setRole("PATIENT");
                return botUserService.saveBotUser(user);
            });

            String role = botUserService.getBotUserByTelegramId(chatId)
                    .map(BotUser::getRole)
                    .orElse("PATIENT");

            switch (role) {
                case "DOCTOR" -> doctorCommandHandler.handle(update, this);
                case "SMM" -> smmCommandHandler.handle(update, this, applicationService);
                case "TARGET" -> targetCommandHandler.handle(update, this, applicationService);
                default -> patientCommandHandler.handle(update, this);
            }
        }
    }

    private void handleCallback(CallbackQuery cb) {
        String data = cb.getData();

        // ищем первый ключ из карты, который является префиксом для data
        String key = commandMap.keySet()
                .stream()
                .filter(data::startsWith)
                .findFirst()
                .orElse(null);

        if (key == null) {
            return;
        }

        ButtonCommand cmd = commandMap.get(key);
        System.out.println("ROUTER: key=" + key + " data=" + data);
        cmd.handle(cb, this);
    }
}
