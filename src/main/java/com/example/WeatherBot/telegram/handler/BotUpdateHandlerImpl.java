package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.utilit.MessageGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class BotUpdateHandlerImpl implements BotUpdateHandler {

    private final ChatService chatService;

    private final MessageGenerator messageGenerator;

    private final CommandHandler commandHandler;

    private final BotStateHandler botStateHandler;

    private final CallbackQueryHandler callbackQueryHandler;

    @Override
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
            messageText = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getMessage().getChatId();

            return callbackQueryHandler.handleCallbackQuery(chatId, messageText);
        }
        else {
            return null;
        }

        if (!chatService.isChatExist(chatId)) {
            chatService.addChat(chatId, BotState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());
        } else {
            return handleMessage(messageText, chatId);
        }
    }

    private SendMessage handleMessage(String messageText, Long chatId) {

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
