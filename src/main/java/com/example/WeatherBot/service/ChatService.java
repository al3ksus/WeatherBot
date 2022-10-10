package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.Chat;

import com.example.WeatherBot.model.enums.CityState;
import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final DBCityService dbCityService;

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
        Optional<DBCity> dbCity = dbCityService.findByNameAndState(city.getLocal_names().getRu(), CityState.DEFAULT);

        if (dbCity.isEmpty()) {
            dbCity = Optional.of(new DBCity(city, CityState.DEFAULT));
        }

        chat.setDefaultCity(dbCity.get());
        dbCityService.add(dbCity.get());
        chatRepository.save(chat);
    }

    public void setTemporaryCity(Long chatId, City city) {
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
        Optional<DBCity> dbCity = dbCityService.findByNameAndState(city.getLocal_names().getRu(), CityState.TEMPORARY);

        if (dbCity.isEmpty()) {
            dbCity = Optional.of(new DBCity(city, CityState.TEMPORARY));
        }

        chat.setTemporaryCity(dbCity.get());
        dbCityService.add(dbCity.get());
        chatRepository.save(chat);
    }

    public Chat getByChatId(Long chatId) {
        return chatRepository.getByChatId(chatId);
    }

    public List<Chat> getList() {
        return chatRepository.findAll();
    }
}