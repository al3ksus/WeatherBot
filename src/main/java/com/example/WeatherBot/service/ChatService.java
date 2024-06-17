package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.Chat;

import com.example.WeatherBot.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void addChat(Long chatId, BotState botState) {
        chatRepository.save(new Chat(chatId, botState));
    }

    public boolean isChatExist(Long chatId) {
        return chatRepository.existsByChatId(chatId);
    }

    public void setBotState(Long chatId, BotState botState){
        Chat chat = chatRepository.getByChatId(chatId);
        chat.setBotState(botState);
        chatRepository.save(chat);
    }

    public void setDefaultCity(Long chatId, DBCity dbCity) {

        Chat chat = chatRepository.getByChatId(chatId);
        //Optional<DBCity> dbCity = dbCityService.findByNameAndState(city.getLocal_names().getRu(), CityState.DEFAULT);

//        if (dbCity.isEmpty()) {
//            dbCity = Optional.of(new DBCity(city, CityState.DEFAULT));
//        }

        chat.setDefaultCity(dbCity);
        chatRepository.save(chat);
    }

    public void setTemporaryCity(Long chatId, DBCity dbCity) {

        Chat chat = chatRepository.getByChatId(chatId);
//        Optional<DBCity> dbCity = dbCityService.findByNameAndState(city.getLocal_names().getRu(), CityState.TEMPORARY);
//
//        if (dbCity.isEmpty()) {
//            dbCity = Optional.of(new DBCity(city, CityState.TEMPORARY));
//        }

        chat.setTemporaryCity(dbCity);
        chatRepository.save(chat);
    }

    public Chat getByChatId(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }

    public List<Chat> getList() {
        return chatRepository.findAll();
    }
}