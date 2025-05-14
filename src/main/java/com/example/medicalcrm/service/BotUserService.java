package com.example.medicalcrm.service;
import com.example.medicalcrm.entity.BotUser;
import com.example.medicalcrm.repository.BotUserRepository;
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

}
