package com.example.WeatherBot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CommandHandler {
    SendMessage handleCommand(Long chatId, String command);
}
