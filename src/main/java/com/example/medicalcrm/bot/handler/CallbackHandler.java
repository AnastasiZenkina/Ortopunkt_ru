package com.example.medicalcrm.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class CallbackHandler {


    public void handle(Update update, AbsSender sender) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());

        if(callbackData.equals("mark_anna")){
            response.setText("Пациент @anna34 отмечен как записанный.");
        }
        else if(callbackData.equals("chat_anna")){
            response.setText("Открываю чат с @anna34 (пока заглушка).");
        }

        try {
            sender.execute(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
