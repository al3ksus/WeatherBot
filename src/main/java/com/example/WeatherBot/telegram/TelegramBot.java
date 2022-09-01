package com.example.WeatherBot.telegram;

import com.example.WeatherBot.config.BotConfig;
import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.Command;
import com.example.WeatherBot.repository.ChatRepository;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
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

            messageText = messageText.substring(1);

            if (messageText.equals(Command.START.toString())) {
                chatService.setBotState(chatId, BotState.DEFAULT);
                sendMessage(chatId, messageGenerator.generateStartMessage());

            } else if (messageText.equals(Command.SETCITY.toString())) {
                chatService.setBotState(chatId, BotState.SETCITY);
                sendMessage(chatId, messageGenerator.generateSetCityMessage());
            }
        }
    }

}
