package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.service.ChatServiceImpl;
import com.example.WeatherBot.utilit.MessageGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@SpringBootTest
public class BotUpdateHandlerImplTest {

    @Autowired
    BotUpdateHandlerImpl botUpdateHandler;

    @Autowired
    ChatServiceImpl chatService;

    @Autowired
    MessageGenerator messageGenerator;

    static Update update;
    static Message message;
    static Chat chat;

    static Long chatId = 1L;


    @BeforeAll
    static void InitializeObjects() {
        update = new Update();
        message = new Message();
        chat = new Chat();

        chat.setId(chatId);
        message.setChat(chat);
        update.setMessage(message);
    }

    @Test
    void handleUpdateWithoutTextTest() {
        chatService.addChat(chatId, BotState.DEFAULT);
        Assertions.assertEquals(botUpdateHandler.handleUpdate(update).getText(), messageGenerator.generateGetInstructionMessage());
        chatService.delete(chatId);
    }

    @Test
    void handleUpdateWithoutMessageTest() {
        update.setMessage(null);
        Assertions.assertNull(botUpdateHandler.handleUpdate(update));
        update.setMessage(message);
    }
}
