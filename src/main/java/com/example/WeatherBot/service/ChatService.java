package com.example.WeatherBot.service;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.chat.Chat;
import com.example.WeatherBot.model.chat.DefaultCity;
import com.example.WeatherBot.repository.ChatRepository;
import com.example.WeatherBot.repository.DefaultCityRepository;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private DefaultCityRepository defaultCityRepository;

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

    public void setDefaultCity(Long chatId, String name, double lat, double lon) {
        Chat chat = chatRepository.getByChatId(chatId);
        DefaultCity defaultCity = chat.getDefaultCity();

        if (defaultCity == null) {
            defaultCity = new DefaultCity(name, lat, lon);
        }
        else {
            defaultCity.setName(name);
            defaultCity.setLat(lat);
            defaultCity.setLon(lon);
        }

        defaultCityRepository.save(defaultCity);
        chat.setDefaultCity(defaultCity);
        chatRepository.save(chat);
    }

    public Chat getByChatId(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }
}