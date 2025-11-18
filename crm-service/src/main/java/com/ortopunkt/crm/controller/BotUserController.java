package com.ortopunkt.crm.controller;

import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.crm.entity.BotUser;
import com.ortopunkt.crm.service.BotUserService;
import com.ortopunkt.logging.ServiceLogger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bot-users")
public class BotUserController {

    private final BotUserService botUserService;
    private final ServiceLogger log = new ServiceLogger(getClass(), "CRM");

    @Autowired
    public BotUserController(BotUserService botUserService) {
        this.botUserService = botUserService;
    }

    @GetMapping
    public List<BotUser> getAllBotUsers() {
        return botUserService.getAllBotUsers();
    }

    @GetMapping("/{telegramId}")
    public ResponseEntity<BotUserResponseDto> getBotUserByTelegramId(@PathVariable("telegramId") Long telegramId) {
        return botUserService.getBotUserByTelegramId(telegramId)
                .map(user -> {
                    BotUserResponseDto dto = new BotUserResponseDto();
                    dto.setTelegramId(user.getTelegramId());
                    dto.setRole(user.getRole());
                    dto.setUsername(user.getUsername());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BotUserResponseDto> create(@RequestBody @Valid BotUserRequestDto dto) {
        BotUserResponseDto saved = botUserService.create(dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{telegramId}")
    public ResponseEntity<Void> deleteBotUser(@PathVariable("telegramId") Long telegramId) {
        botUserService.deleteBotUser(telegramId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{telegramId}/role")
    public ResponseEntity<BotUserResponseDto> updateRole(
            @PathVariable("telegramId") Long telegramId,
            @RequestParam("role") String role
    ) {
        log.info("Запрос на смену роли: user=" + telegramId + ", новая роль=" + role);
        BotUserResponseDto updated = botUserService.updateRole(telegramId, role);
        return ResponseEntity.ok(updated);
    }
}