package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.chat.DefaultCity;
import com.example.WeatherBot.model.city.CityInfo;
import com.example.WeatherBot.model.weather.Forecast;
import com.example.WeatherBot.model.weather.MainWeather;
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

    @Autowired
    private Optional<CityInfo> optionalCityInfo;

    private String cityName;

    public SendMessage handleBotState(Long chatId, String messageText, BotState botState) {

        if(botState.equals(BotState.SETCITY) || botState.equals(BotState.SETDEFAULTCITY)) {
            return handleSetCity(chatId, messageText, botState);
        }
        else {
            return handleGetWeather(chatId, messageText);
        }
    }

    public SendMessage handleSetCity(Long chatId, String messageText, BotState botState) {
        CityInfo[] cityInfo = weatherService.isCityExist(messageText);

        if (cityInfo.length != 0) {
            optionalCityInfo = Arrays.stream(cityInfo).filter(info -> info.getCountry().equals("RU")).findFirst();

            if (optionalCityInfo.isPresent()) {

                cityName = optionalCityInfo.get().getLocal_names() == null?
                        messageText.charAt(0) + messageText.substring(1).toLowerCase():
                        optionalCityInfo.get().getLocal_names().getRu();

                if(botState.equals(BotState.SETCITY)) {
                    chatService.setBotState(chatId, BotState.GETWEATHER);
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateWeatherMessage());
                    sendMessage.setReplyMarkup(keyBoardService.getWeatherButtonRow());
                    return sendMessage;
                }
                else {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    chatService.setDefaultCity(chatId, cityName, optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
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

    public SendMessage handleGetWeather(Long chatId, String messageText) {
        chatService.setBotState(chatId, BotState.DEFAULT);

        if (messageText.equals("CURRENTWEATHER")) {
            MainWeather mainWeather;

            if (optionalCityInfo.isPresent()) {
                mainWeather = weatherService.getCurrentWeather(optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                optionalCityInfo = Optional.empty();
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, cityName));
            }
            else {
                DefaultCity defaultCity = chatService.getByChatId(chatId).getDefaultCity();
                mainWeather = weatherService.getCurrentWeather(defaultCity.getLat(), defaultCity.getLon());
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(mainWeather, defaultCity.getName()));
            }
        }
        else {
            Forecast forecast;

            if (optionalCityInfo.isPresent()) {
                forecast = weatherService.getForecast(optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                optionalCityInfo = Optional.empty();
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast));
            }
            else {
                DefaultCity defaultCity = chatService.getByChatId(chatId).getDefaultCity();
                forecast = weatherService.getForecast(defaultCity.getLat(), defaultCity.getLon());
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateForecastMessage(forecast));

            }
        }
    }
}
