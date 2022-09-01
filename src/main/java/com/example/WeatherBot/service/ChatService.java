package com.example.WeatherBot.service;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.Chat;
import com.example.WeatherBot.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public void addChat(Long chatId, BotState botState) {
        chatRepository.save(new Chat(chatId, botState));
    }

    public boolean isChatExist(Long chatId) {
        return chatRepository.existsByChatId(chatId);
    }
}