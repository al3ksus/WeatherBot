package com.example.WeatherBot.telegram;

import com.example.WeatherBot.config.BotConfig;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.telegram.service.BotService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final BotService botService;


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
            SendMessage sendMessage = botService.handleUpdate(update);

            if (sendMessage != null) {
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
                e.printStackTrace();
        }
    }
}

