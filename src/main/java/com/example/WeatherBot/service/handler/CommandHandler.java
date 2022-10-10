package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.KeyBoardService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@AllArgsConstructor
public class CommandHandler {

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final KeyBoardService keyBoardService;

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
            case "HELP" ->{
                return handleHelp(chatId);
            }
            default -> {
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoSuchCommandMessage());
            }
        }
    }

    private SendMessage handleStart(Long chatId) {
        chatService.setBotState(chatId, BotState.DEFAULT);
        return new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
    }

    private SendMessage handleSetCity(Long chatId) {
        chatService.setBotState(chatId, BotState.SET_CITY);
        return new SendMessage(String.valueOf(chatId), messageGenerator.generateSetCityMessage());
    }

    private SendMessage handleSetDefaultCity(Long chatId) {
        chatService.setBotState(chatId, BotState.SET_DEFAULT_CITY);
        return new SendMessage(String.valueOf(chatId), messageGenerator.generateSetCityMessage());
    }

    private SendMessage handleGetWeather(Long chatId) {
        DBCity city = chatService.getByChatId(chatId).getDefaultCity();
        SendMessage sendMessage;

        if (city != null) {
            chatService.setBotState(chatId, BotState.GET_WEATHER_DEFAULT);
            sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateWeatherMessage());
            sendMessage.setReplyMarkup(keyBoardService.getWeatherButtonRow());
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoDefaultCityMessage());
        }

        return sendMessage;
    }

    private SendMessage handleHelp(Long chatId) {
        return new SendMessage(String.valueOf(chatId),  messageGenerator.generateHelpMessage());
    }
}
