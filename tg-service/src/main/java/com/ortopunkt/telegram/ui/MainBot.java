package com.ortopunkt.telegram.ui;

import com.ortopunkt.logging.ServiceLogger;
import com.ortopunkt.telegram.client.CrmClient;
import com.ortopunkt.telegram.ui.button.ButtonCommand;
import com.ortopunkt.telegram.ui.button.handler.manual.ManualVkCommand;
import com.ortopunkt.telegram.ui.button.handler.manual.ManualInstaCommand;
import com.ortopunkt.telegram.ui.screen.DoctorScreen;
import com.ortopunkt.telegram.ui.screen.PatientScreen;
import com.ortopunkt.telegram.ui.screen.SmmScreen;
import com.ortopunkt.telegram.ui.screen.TargetScreen;
import com.ortopunkt.telegram.config.BotConfig;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component("mainBot")
@RequiredArgsConstructor
@Getter
public class MainBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CrmClient crmClient;
    private final ManualVkCommand manualVkCommand;
    private final ManualInstaCommand manualInstaCommand;
    private final DoctorScreen doctorCommandHandler;
    private final SmmScreen smmCommandHandler;
    private final TargetScreen targetCommandHandler;
    private final PatientScreen patientCommandHandler;
    private final BotRoleResolver botRoleResolver;

    private final ServiceLogger log = new ServiceLogger(getClass(), "TG");

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
            String role = botRoleResolver.resolveRole(chatId, username);

            log.info("Новое сообщение от @" + username + " (role=" + role + ")");

            if (manualVkCommand.handle(update, this)) return;
            if (manualInstaCommand.handle(update, this)) return;

            switch (role) {
                case "DOCTOR" -> doctorCommandHandler.handle(update, this);
                case "SMM"    -> smmCommandHandler.handle(update, this, crmClient);
                case "TARGET" -> targetCommandHandler.handle(update, this, crmClient);
                default       -> patientCommandHandler.handle(update, this);
            }
        }
    }

    private void handleCallback(CallbackQuery cb) {
        String data = cb.getData();
        String key = commandMap.keySet()
                .stream()
                .filter(data::startsWith)
                .findFirst()
                .orElse(null);

        if (key == null) return;

        ButtonCommand cmd = commandMap.get(key);
        log.info("ROUTER: key=" + key + " data=" + data);
        cmd.handle(cb, this);
    }
}