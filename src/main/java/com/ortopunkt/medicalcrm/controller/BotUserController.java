package com.ortopunkt.medicalcrm.controller;
import com.ortopunkt.medicalcrm.dto.BotUserRequestDto;
import com.ortopunkt.medicalcrm.dto.BotUserResponseDto;
import com.ortopunkt.medicalcrm.entity.BotUser;
import com.ortopunkt.medicalcrm.service.BotUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bot-users")
public class BotUserController {
    private final BotUserService botUserService;

    @Autowired
    public BotUserController(BotUserService botUserService){
        this.botUserService = botUserService;
    }

    @GetMapping
    public List <BotUser> getAllBotUsers(){
        return botUserService.getAllBotUsers();
    }


    @GetMapping("/{telegramId}")
    public ResponseEntity<BotUser> getBotUserByTelegramId(@PathVariable Long telegramId) {
        return botUserService.getBotUserByTelegramId(telegramId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<BotUserResponseDto> create(@RequestBody @Valid BotUserRequestDto dto) {
        BotUser saved = botUserService.create(dto);
        return ResponseEntity.ok(BotUserResponseDto.fromEntity(saved));
    }


    @DeleteMapping("/{telegramId}")
    public ResponseEntity<Void> deleteBotUser(@PathVariable Long telegramId) {
        botUserService.deleteBotUser(telegramId);
        return ResponseEntity.noContent().build();
    }

}
