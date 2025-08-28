package com.ortopunkt.crm.controller;
import com.ortopunkt.dto.request.BotUserRequestDto;
import com.ortopunkt.dto.response.BotUserResponseDto;
import com.ortopunkt.crm.entity.BotUser;
import com.ortopunkt.crm.service.BotUserService;
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
        BotUserResponseDto saved = botUserService.create(dto);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/{telegramId}")
    public ResponseEntity<Void> deleteBotUser(@PathVariable Long telegramId) {
        botUserService.deleteBotUser(telegramId);
        return ResponseEntity.noContent().build();
    }

}
