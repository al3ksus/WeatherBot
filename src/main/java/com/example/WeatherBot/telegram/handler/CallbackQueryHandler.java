package com.example.WeatherBot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CallbackQueryHandler {
    SendMessage handleCallbackQuery(Long chatId, String data);
}
