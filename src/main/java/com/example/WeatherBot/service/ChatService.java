package com.example.WeatherBot.service;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.model.DBModel.DefaultCity;
import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private DefaultCityService defaultCityService;

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

    public void setDefaultCity(Long chatId, City city) {
        /*Chat chat = chatRepository.getByChatId(chatId);
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
        chatRepository.save(chat);*/

        Chat chat = chatRepository.getByChatId(chatId);
        Optional<DefaultCity> defaultCity = defaultCityService.findByName(city.getLocal_names().getRu());

        if (defaultCity.isEmpty()) {
            defaultCity = Optional.of(new DefaultCity(city));
        }

        chat.setDefaultCity(defaultCity.get());
        defaultCityService.add(defaultCity.get());
        chatRepository.save(chat);

    }

    public Chat getByChatId(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }
}