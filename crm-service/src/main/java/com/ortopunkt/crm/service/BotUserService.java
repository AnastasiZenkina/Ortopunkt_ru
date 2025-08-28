package com.ortopunkt.crm.service;

import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.crm.entity.BotUser;
import com.ortopunkt.crm.repository.BotUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BotUserService {

    @Autowired
    private BotUserRepository botUserRepository;

    public List<BotUser> getAllBotUsers() {
        return botUserRepository.findAll();
    }

    public Optional<BotUser> getBotUserByTelegramId(Long telegramId) {
        return botUserRepository.findById(telegramId);
    }

    public BotUser saveBotUser(BotUser botUser) {
        return botUserRepository.save(botUser);
    }

    public void deleteBotUser(Long id) {
        botUserRepository.deleteById(id);
    }

    public BotUserResponseDto create(BotUserRequestDto dto) {
        BotUser botUser = new BotUser();
        botUser.setRole(dto.getRole());
        botUser.setUsername(dto.getUsername());

        botUser = botUserRepository.save(botUser);
        return toResponseDto(botUser);
    }

    private static BotUserResponseDto toResponseDto(BotUser botUser){
        BotUserResponseDto dto = new BotUserResponseDto();
        dto.setTelegramId(botUser.getTelegramId());
        dto.setRole(botUser.getRole());
        dto.setUsername(botUser.getUsername());
        return dto;
    }
}
