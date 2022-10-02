package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.DefaultCity;
import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.KeyBoardService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import com.example.WeatherBot.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.Optional;

@Component
public class BotStateHandler {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private ChatService chatService;

    @Autowired
    private KeyBoardService keyBoardService;

    private City city = null;

    private String cityName;

    public SendMessage handleBotState(Long chatId, String messageText, BotState botState) {

        if(botState.equals(BotState.SET_CITY) || botState.equals(BotState.SET_DEFAULT_CITY)) {
            return handleSetCity(chatId, messageText, botState);
        }
        else if (botState.equals(BotState.GET_WEATHER_DEFAULT)) {
            return handleGetWeatherDefault(chatId, messageText);
        }
        else if (botState.equals(BotState.GET_WEATHER)) {
            return handleGetWeather(chatId, messageText);
        }
        else {
            return null;
        }
    }

    private SendMessage handleSetCity(Long chatId, String messageText, BotState botState) {
        City[] city = weatherService.isCityExist(messageText);

        if (city.length != 0) {
            Optional<City> optionalCityInfo = Arrays.stream(city).filter(info -> info.getCountry().equals("RU")).findFirst();

            if (optionalCityInfo.isPresent()) {
                this.city = optionalCityInfo.get();
                cityName = optionalCityInfo.get().getLocal_names() == null?
                        messageText.charAt(0) + messageText.substring(1).toLowerCase():
                        optionalCityInfo.get().getLocal_names().getRu();

                if(botState.equals(BotState.SET_CITY)) {
                    chatService.setBotState(chatId, BotState.GET_WEATHER);
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateWeatherMessage());
                    sendMessage.setReplyMarkup(keyBoardService.getWeatherButtonRow());
                    return sendMessage;
                }
                else {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    //chatService.setDefaultCity(chatId, cityName, optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                    chatService.setDefaultCity(chatId, optionalCityInfo.get());
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateDefaultCityMessage(cityName));
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

    private SendMessage handleGetWeather(Long chatId, String messageText) {
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
        DefaultCity defaultCity = chatService.getByChatId(chatId).getDefaultCity();

        if (messageText.equals("CURRENTWEATHER")) {
            MainWeather mainWeather;

            mainWeather = weatherService.getCurrentWeather(defaultCity.getCity().getLat(), defaultCity.getCity().getLon());
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, defaultCity.getName()));
        }
        else if (messageText.equals("FORECAST")) {
            Forecast forecast;

            forecast = weatherService.getForecast(defaultCity.getCity().getLat(), defaultCity.getCity().getLon());
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast, defaultCity.getName()));
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
        }
    }
}
