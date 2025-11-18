package com.ortopunkt.crm.repository;

import com.ortopunkt.crm.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BotUserRepository extends JpaRepository<BotUser, Long> {
    Optional<BotUser> findByTelegramId(Long telegramId);
}