package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.city.CityInfo;
import com.example.WeatherBot.model.weather.CurrentWeather;
import com.example.WeatherBot.service.ChatService;
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

    public SendMessage handleBotState(Long chatId, String messageText, BotState botState) {

        if(botState.equals(BotState.SETCITY) || botState.equals(BotState.SETDEFAULTCITY)) {
            return handleSetCity(chatId, messageText, botState);
        }

        return null;
    }

    public SendMessage handleSetCity(Long chatId, String messageText, BotState botState) {
        CityInfo[] cityInfo = weatherService.isCityExist(messageText);

        if (cityInfo.length != 0) {
            Optional<CityInfo> optionalCityInfo = Arrays.stream(cityInfo).filter(info -> info.getCountry().equals("RU")).findFirst();

            if (optionalCityInfo.isPresent()) {

                String cityName = optionalCityInfo.get().getLocal_names() == null?
                        messageText.charAt(0) + messageText.substring(1).toLowerCase():
                        optionalCityInfo.get().getLocal_names().getRu();

                if(botState.equals(BotState.SETCITY)) {
                    CurrentWeather currentWeather = weatherService.getCurrentWeather(optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(currentWeather, cityName));
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
}
