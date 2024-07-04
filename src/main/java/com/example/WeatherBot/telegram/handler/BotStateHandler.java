package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.enums.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotStateHandler {
    SendMessage handleBotState(Long chatId, String messageText, BotState botState);
}
