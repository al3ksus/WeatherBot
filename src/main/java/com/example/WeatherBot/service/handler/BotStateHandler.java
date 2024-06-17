package com.example.WeatherBot.service.handler;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.DBCityService;
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

    private final DBCityService dbCityService;

    private final KeyBoardService keyBoardService;

    public SendMessage handleBotState(Long chatId, String messageText, BotState botState) {

        if(botState.equals(BotState.SET_CITY) || botState.equals(BotState.SET_DEFAULT_CITY)) {
            return handleSetCity(chatId, messageText, botState);
        }
        else {
            return null;
        }
    }

    private SendMessage handleSetCity(Long chatId, String messageText, BotState botState) {
        City[] city = weatherService.getCityList(messageText);

        if (city.length != 0) {
            Optional<City> optionalCity = Arrays.stream(city).filter(c -> c.getCountry().equals("RU")).findFirst();

            if (optionalCity.isPresent()) {
                
                if (optionalCity.get().getLocal_names() == null) {
                    optionalCity.get().setLocal_names(new LocalNames(messageText.charAt(0) + messageText.substring(1).toLowerCase()));
                }
                DBCity dbCity = dbCityService.findByName(
                        optionalCity.get().getLocal_names().getRu()
                ).orElse(new DBCity(optionalCity.get()));
                dbCityService.add(dbCity);

                if(botState.equals(BotState.SET_CITY)) {
                    chatService.setBotState(chatId, BotState.GET_WEATHER);
                    chatService.setTemporaryCity(chatId, dbCity);
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateWeatherMessage());
                    sendMessage.setReplyMarkup(keyBoardService.getWeatherButtonRow());
                    return sendMessage;
                }
                else {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    chatService.setDefaultCity(chatId, dbCity);
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
}
