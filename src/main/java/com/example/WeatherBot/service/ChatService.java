package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;

import java.util.List;

public interface ChatService {
    void addChat(Long chatId, BotState botState);
    boolean isChatExist(Long chatId);
    void setBotState(Long chatId, BotState botState);
    void setDefaultCity(Long chatId, DBCity dbCity);
    void setTemporaryCity(Long chatId, DBCity dbCity);
    Chat getByChatId(Long chatId);
    List<Chat> getList();
    void delete(Long chatId);
}
