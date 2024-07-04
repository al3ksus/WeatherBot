package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.service.ChatServiceImpl;
import com.example.WeatherBot.utilit.MessageGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@SpringBootTest
public class BotUpdateHandlerImplTest {

    @Autowired
    BotUpdateHandlerImpl botUpdateHandlerImpl;

    @Autowired
    ChatServiceImpl chatServiceImpl;

    @Autowired
    MessageGenerator messageGenerator;

    static Update update;
    static Message message;
    static Chat chat;

    @BeforeAll
    static void InitializeObjects() {
        update = new Update();
        message = new Message();
        chat = new Chat();

        message.setChat(chat);
        update.setMessage(message);
    }

    @Test
    void handleUpdateWithoutTextTest() {
        chatServiceImpl.addChat(1L, BotState.DEFAULT);
        chat.setId(1L);
        message.setChat(chat);
        update.setMessage(message);
        Assertions.assertEquals(botUpdateHandlerImpl.handleUpdate(update).getText(), messageGenerator.generateGetInstructionMessage());
        chatServiceImpl.delete(1L);
    }

    @Test
    void handleStartCommandTest() {
        chatServiceImpl.addChat(1L, BotState.DEFAULT);
        chat.setId(1L);
        message.setText("/start");
        Assertions.assertTrue(
                botUpdateHandlerImpl.handleUpdate(update).getText().equals(messageGenerator.generateStartMessage())
                        && chatServiceImpl.getByChatId(1L).getBotState() == BotState.DEFAULT
        );

        message.setText("/setcity");
        Assertions.assertTrue(
                botUpdateHandlerImpl.handleUpdate(update).getText().equals(messageGenerator.generateSetCityMessage())
                        && chatServiceImpl.getByChatId(1L).getBotState() == BotState.SET_CITY
        );
        chatServiceImpl.delete(1L);
    }
}
