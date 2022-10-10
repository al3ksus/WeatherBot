package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@AllArgsConstructor
public class CallbackQueryHandler {

    private final ChatService chatService;

    private final WeatherService weatherService;

    private final MessageGenerator messageGenerator;

    public SendMessage handleCallbackQuery(Long chatId, String data) {

        return switch (data) {
            case "currentWeather" -> handleCurrentWeather(chatId);
            case "forecast" -> handleForecast(chatId);
            default -> new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
        };
    }

    private SendMessage handleCurrentWeather(Long chatId) {
        DBCity dbCity;

        if (chatService.getByChatId(chatId).getBotState().equals(BotState.GET_WEATHER_DEFAULT)) {
            dbCity = chatService.getByChatId(chatId).getDefaultCity();
        }
        else {
            dbCity = chatService.getByChatId(chatId).getTemporaryCity();
        }

        MainWeather mainWeather = weatherService.getCurrentWeather(dbCity.getCity().getLat(), dbCity.getCity().getLon());
        chatService.setBotState(chatId, BotState.DEFAULT);

        return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, dbCity.getName()));
    }

    private SendMessage handleForecast(Long chatId) {
        DBCity dbCity;

        if (chatService.getByChatId(chatId).getBotState().equals(BotState.GET_WEATHER_DEFAULT)) {
            dbCity = chatService.getByChatId(chatId).getDefaultCity();
        }
        else {
            dbCity = chatService.getByChatId(chatId).getTemporaryCity();
        }

        Forecast forecast = weatherService.getForecast(dbCity.getCity().getLat(), dbCity.getCity().getLon());
        chatService.setBotState(chatId, BotState.DEFAULT);

        return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast, dbCity.getName()));
    }
}
