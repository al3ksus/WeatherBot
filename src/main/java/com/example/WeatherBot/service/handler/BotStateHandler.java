package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.KeyBoardService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import com.example.WeatherBot.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BotStateHandler {

    private final WeatherService weatherService;

    private final MessageGenerator messageGenerator;

    private final ChatService chatService;

    private final KeyBoardService keyBoardService;

    public SendMessage handleBotState(Long chatId, String messageText, BotState botState) {

        if(botState.equals(BotState.SET_CITY) || botState.equals(BotState.SET_DEFAULT_CITY)) {
            return handleSetCity(chatId, messageText, botState);
        }
        /*else if (botState.equals(BotState.GET_WEATHER_DEFAULT)) {
            return handleGetWeatherDefault(chatId, messageText);
        }
        else if (botState.equals(BotState.GET_WEATHER)) {
            return handleGetWeather(chatId, messageText);
        }*/
        else {
            return null;
        }
    }

    private SendMessage handleSetCity(Long chatId, String messageText, BotState botState) {
        City[] city = weatherService.isCityExist(messageText);

        if (city.length != 0) {
            Optional<City> optionalCity = Arrays.stream(city).filter(c -> c.getCountry().equals("RU")).findFirst();

            if (optionalCity.isPresent()) {
                
                if (optionalCity.get().getLocal_names() == null) {
                    optionalCity.get().setLocal_names(new LocalNames(messageText.charAt(0) + messageText.substring(1).toLowerCase()));
                }

                if(botState.equals(BotState.SET_CITY)) {
                    chatService.setBotState(chatId, BotState.GET_WEATHER);
                    chatService.setTemporaryCity(chatId, optionalCity.get());
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateWeatherMessage());
                    sendMessage.setReplyMarkup(keyBoardService.getWeatherButtonRow());
                    return sendMessage;
                }
                else {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    //chatService.setDefaultCity(chatId, cityName, optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                    chatService.setDefaultCity(chatId, optionalCity.get());
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateDefaultCityMessage(optionalCity.get().getLocal_names().getRu()));
                }
            }
            else {
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateNonRussianCityMessage());
            }
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoSuchCityMessage());
        }
    }

    /*private SendMessage handleGetWeather(Long chatId, String messageText) {
        chatService.setBotState(chatId, BotState.DEFAULT);

        if (messageText.equals("CURRENTWEATHER")) {
            MainWeather mainWeather;

            mainWeather = weatherService.getCurrentWeather(city.getLat(), city.getLon());
            city = null;
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, cityName));
        }
        else if (messageText.equals("FORECAST")) {
            Forecast forecast;

            forecast = weatherService.getForecast(city.getLat(), city.getLon());
            city = null;
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast, cityName));
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
        }
    }

    private SendMessage handleGetWeatherDefault(Long chatId, String messageText) {
        chatService.setBotState(chatId, BotState.DEFAULT);
        DBCity dbCity = chatService.getByChatId(chatId).getDefaultCity();

        if (messageText.equals("CURRENTWEATHER")) {
            MainWeather mainWeather;

            mainWeather = weatherService.getCurrentWeather(dbCity.getCity().getLat(), dbCity.getCity().getLon());
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, dbCity.getName()));
        }
        else if (messageText.equals("FORECAST")) {
            Forecast forecast;

            forecast = weatherService.getForecast(dbCity.getCity().getLat(), dbCity.getCity().getLon());
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast, dbCity.getName()));
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
        }
    }*/
}
