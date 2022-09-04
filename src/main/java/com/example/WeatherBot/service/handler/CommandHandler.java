package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.chat.Chat;
import com.example.WeatherBot.model.chat.DefaultCity;
import com.example.WeatherBot.model.weather.CurrentWeather;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.KeyBoardService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class CommandHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private KeyBoardService keyBoardService;

    @Autowired
    private WeatherService weatherService;

    public SendMessage handleCommand(Long chatId, String command) {

        switch (command) {
            case "START" -> {
                return handleStart(chatId);
            }
            case "SETCITY" -> {
                return handleSetCity(chatId);
            }
            case "SETDEFAULTCITY" -> {
                return handleSetDefaultCity(chatId);
            }
            case "GETWEATHER" -> {
                return handleGetWeather(chatId);
            }
            default -> {
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoSuchCommandMessage());
            }
        }
    }

    public SendMessage handleStart(Long chatId) {
        chatService.setBotState(chatId, BotState.DEFAULT);
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
        sendMessage.setReplyMarkup(keyBoardService.getButton("Выбрать город", "/setDefaultCity"));
        return sendMessage;
    }

    public SendMessage handleSetCity(Long chatId) {
        chatService.setBotState(chatId, BotState.SETCITY);
        return new SendMessage(String.valueOf(chatId), messageGenerator.generateSetCityMessage());
    }

    public SendMessage handleSetDefaultCity(Long chatId) {
        chatService.setBotState(chatId, BotState.SETDEFAULTCITY);
        return new SendMessage(String.valueOf(chatId), messageGenerator.generateSetCityMessage());
    }

    public SendMessage handleGetWeather(Long chatId) {
        DefaultCity city = chatService.getByChatId(chatId).getDefaultCity();

        if (city != null) {
            CurrentWeather currentWeather = weatherService.getCurrentWeather(city.getLat(), city.getLon());
            chatService.setBotState(chatId, BotState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(currentWeather, city.getName()));
        }
        else {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateNoDefaultCityMessage());
            sendMessage.setReplyMarkup(keyBoardService.getButton("Выбрать город", "/setDefaultCity"));
            return sendMessage;
        }
    }
}
