package com.example.WeatherBot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotUpdateHandler {
    SendMessage handleUpdate(Update update);
}
