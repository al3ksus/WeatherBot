package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.Chat;

import com.example.WeatherBot.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public void addChat(Long chatId, BotState botState) {
        chatRepository.save(new Chat(chatId, botState));
    }

    @Override
    public boolean isChatExist(Long chatId) {
        return chatRepository.existsByChatId(chatId);
    }

    @Override
    public void setBotState(Long chatId, BotState botState){
        Chat chat = chatRepository.getByChatId(chatId);
        chat.setBotState(botState);
        chatRepository.save(chat);
    }

    @Override
    public void setDefaultCity(Long chatId, DBCity dbCity) {

        Chat chat = chatRepository.getByChatId(chatId);
        chat.setDefaultCity(dbCity);
        chatRepository.save(chat);
    }

    @Override
    public void setTemporaryCity(Long chatId, DBCity dbCity) {

        Chat chat = chatRepository.getByChatId(chatId);
        chat.setTemporaryCity(dbCity);
        chatRepository.save(chat);
    }

    @Override
    public Chat getByChatId(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }

    @Override
    public List<Chat> getList() {
        return chatRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        chatRepository.deleteByChatId(chatId);
    }
}