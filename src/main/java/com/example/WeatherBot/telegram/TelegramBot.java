package com.example.WeatherBot.telegram;

import com.example.WeatherBot.config.BotConfig;
import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.Chat;
import com.example.WeatherBot.model.Command;
import com.example.WeatherBot.repository.ChatRepository;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import com.example.WeatherBot.weather.CurrentWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private ChatService chatService;

    @Autowired
    private WeatherService weatherService;


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        handleUpdate(update);
    }

    public void sendMessage (Long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdate(Update update) {
        String messageText = "";
        Long chatId = null;

        if (update.hasMessage()) {
            messageText = update.getMessage().getText().toUpperCase();
            chatId = update.getMessage().getChatId();
        }

        if (!chatService.isChatExist(chatId)) {
            chatService.addChat(chatId, BotState.DEFAULT);
            sendMessage(update.getMessage().getChatId(), messageGenerator.generateStartMessage());
        } else {
            handleBotState(update, messageText, chatId);
        }
    }

    public void handleBotState(Update update, String messageText, Long chatId) {

        if (messageText.charAt(0) == '/') {

            String command = messageText.substring(1);

            switch (command) {
                case "START" -> {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    sendMessage(chatId, messageGenerator.generateStartMessage());
                }
                case "SETCITY" -> {
                    chatService.setBotState(chatId, BotState.SETCITY);
                    sendMessage(chatId, messageGenerator.generateSetCityMessage());
                }
                default -> sendMessage(chatId, messageGenerator.generateNoSuchCommandMessage());
            }

        } else {
            Chat chat = chatService.getByChatId(chatId);
            BotState botState = chat.getBotState();

            switch (botState) {
                case SETCITY -> {
                    CurrentWeather currentWeather =  weatherService.getCurrentWeather(messageText);

                    if (currentWeather != null) {
                        sendMessage(chatId, "В городе " + messageText.charAt(0) + messageText.substring(1).toLowerCase() + " солнечно!");
                    } else {
                        sendMessage(chatId, messageGenerator.generateNoSuchCityMessage());
                    }
                }
            }
        }
    }

}
