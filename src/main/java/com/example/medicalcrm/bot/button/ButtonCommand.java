package com.example.medicalcrm.bot.button;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface ButtonCommand {
    void handle(CallbackQuery callbackQuery, AbsSender sender);
}