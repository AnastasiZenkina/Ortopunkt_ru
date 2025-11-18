package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.crm.entity.BotUser;
import com.ortopunkt.crm.repository.BotUserRepository;
import com.ortopunkt.logging.ServiceLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BotUserService {

    private final BotUserRepository botUserRepository;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    public List<BotUser> getAllBotUsers() {
        log.info("Запрос всех пользователей бота");
        return botUserRepository.findAll();
    }

    public Optional<BotUser> getBotUserByTelegramId(Long telegramId) {
        log.info("Получение пользователя Telegram id=" + telegramId);
        return botUserRepository.findByTelegramId(telegramId);
    }

    public BotUser saveBotUser(BotUser botUser) {
        log.info("Сохранение пользователя Telegram id=" + botUser.getTelegramId());
        return botUserRepository.save(botUser);
    }

    public void deleteBotUser(Long id) {
        log.info("Удаление пользователя Telegram id=" + id);
        botUserRepository.deleteById(id);
    }

    public BotUserResponseDto create(BotUserRequestDto dto) {
        log.info("Создание нового пользователя Telegram id=" + dto.getTelegramId());
        BotUser botUser = new BotUser();
        botUser.setTelegramId(dto.getTelegramId());
        botUser.setRole(dto.getRole());
        botUser.setUsername(dto.getUsername());
        botUser = botUserRepository.save(botUser);
        return toResponseDto(botUser);
    }

    private static BotUserResponseDto toResponseDto(BotUser botUser) {
        BotUserResponseDto dto = new BotUserResponseDto();
        dto.setTelegramId(botUser.getTelegramId());
        dto.setRole(botUser.getRole());
        dto.setUsername(botUser.getUsername());
        return dto;
    }

    public BotUserResponseDto updateRole(Long telegramId, String newRole) {
        log.info("Обновление роли пользователя Telegram id=" + telegramId + " → " + newRole);
        BotUser botUser = botUserRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newRole.equalsIgnoreCase(botUser.getRole())) {
            return toResponseDto(botUser);
        }

        botUser.setRole(newRole);
        botUserRepository.save(botUser);
        return toResponseDto(botUser);
    }
}