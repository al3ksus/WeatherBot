package com.example.WeatherBot.service;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.Chat;
import com.example.WeatherBot.model.city.CityInfo;
import com.example.WeatherBot.model.weather.CurrentWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Optional;

@Component
public class BotService {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private KeyBoardService keyBoardService;

    public SendMessage handleUpdate(Update update) {
        String messageText = "";
        Long chatId = null;

        if (update.hasMessage()) {
            messageText = update.getMessage().getText().toUpperCase();
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            messageText = update.getCallbackQuery().getData().toUpperCase();
            chatId = update.getCallbackQuery().getMessage().getChatId();
            System.out.println(messageText);
            System.out.println(chatId);
        }

        if (!chatService.isChatExist(chatId)) {
            chatService.addChat(chatId, BotState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
        } else {
            return handleBotState(update, messageText, chatId);
        }
    }

    public SendMessage handleBotState(Update update, String messageText, Long chatId) {

        if (messageText.charAt(0) == '/') {

            String command = messageText.substring(1);

            switch (command) {
                case "START" -> {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
                    sendMessage.setReplyMarkup(keyBoardService.getSetCityButton());
                    return sendMessage;
                }
                case "SETCITY" -> {
                    chatService.setBotState(chatId, BotState.SETCITY);
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateSetCityMessage());
                }
                default -> {
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoSuchCommandMessage());
                }
            }

        } else {
            Chat chat = chatService.getByChatId(chatId);
            BotState botState = chat.getBotState();

            switch (botState) {
                case SETCITY -> {
                    CityInfo[] cityInfo = weatherService.isCityExist(messageText);

                    if (cityInfo.length != 0) {
                        Optional<CityInfo> optionalCityInfo = Arrays.stream(cityInfo).filter(info -> info.getCountry().equals("RU")).findFirst();

                        if (optionalCityInfo.isPresent()) {

                            CurrentWeather currentWeather = weatherService.getCurrentWeather(optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                            String cityName = optionalCityInfo.get().getLocal_names() == null?
                                    messageText.charAt(0) + messageText.substring(1).toLowerCase():
                                    optionalCityInfo.get().getLocal_names().getRu();

                            chatService.setBotState(chatId, BotState.DEFAULT);
                            return new SendMessage(String.valueOf(chatId), messageGenerator.generateCurrentWeatherMessage(currentWeather, cityName));

                        } else {
                            return new SendMessage(String.valueOf(chatId), messageGenerator.generateNonRussianCityMessage());
                        }
                    } else {
                        return new SendMessage(String.valueOf(chatId), messageGenerator.generateNoSuchCityMessage());
                    }

                }
            }
        }

        return  null;
    }
}
