package com.example.medicalcrm.controller;
import com.example.medicalcrm.entity.BotUser;
import com.example.medicalcrm.service.BotUserService;
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

    @GetMapping("/{id}")
    public ResponseEntity<BotUser> getBotUserById(@PathVariable Long id){
        return botUserService.getBotUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BotUser createBotUser(@RequestBody BotUser botUser){
        return botUserService.saveBotUser(botUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBotUser(@PathVariable Long id){
        botUserService.deleteBotUser(id);
        return ResponseEntity.noContent().build();
    }
}
