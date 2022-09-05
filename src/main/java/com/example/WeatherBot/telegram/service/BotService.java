package com.example.WeatherBot.telegram.service;

import com.example.WeatherBot.model.BotState;
import com.example.WeatherBot.model.chat.Chat;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.KeyBoardService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.service.handler.BotStateHandler;
import com.example.WeatherBot.service.handler.CommandHandler;
import com.example.WeatherBot.telegram.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    @Autowired
    private CommandHandler commandHandler;

    @Autowired
    private BotStateHandler botStateHandler;

    public SendMessage handleUpdate(Update update) {
        String messageText = "";
        Long chatId = null;

        if (update.hasMessage()) {

            chatId = update.getMessage().getChatId();

            if (!update.getMessage().hasText()) {
                return new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
            }

            messageText = update.getMessage().getText().toUpperCase();
        }
        else if (update.hasCallbackQuery()) {
            messageText = update.getCallbackQuery().getData().toUpperCase();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        else {
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateGetInstructionMessage());
        }

        if (!chatService.isChatExist(chatId)) {
            chatService.addChat(chatId, BotState.DEFAULT);
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
            sendMessage.setReplyMarkup(keyBoardService.getButton("Выбрать город", "/setDefaultCity"));
            return sendMessage;
        } else {
            return handleMessage(messageText, chatId);
        }
    }

    public SendMessage handleMessage(String messageText, Long chatId) {

        if (messageText.charAt(0) == '/') {

            String command = messageText.substring(1);

            return commandHandler.handleCommand(chatId, command);

        } else {
            Chat chat = chatService.getByChatId(chatId);
            BotState botState = chat.getBotState();

            return botStateHandler.handleBotState(chatId, messageText, botState);
        }
    }
}
