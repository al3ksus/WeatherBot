package com.example.WeatherBot.telegram;

import com.example.WeatherBot.config.BotConfig;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.telegram.service.BotService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import com.vdurmont.emoji.EmojiParser;
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

    @Autowired
    private BotService botService;


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

        try {
            execute(botService.handleUpdate(update));
        } catch (TelegramApiException e) {
            try {
                execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Чтобы увидеть список комманд используйте /help"));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }
}
