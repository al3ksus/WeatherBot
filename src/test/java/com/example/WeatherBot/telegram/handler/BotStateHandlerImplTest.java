package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.service.*;
import com.example.WeatherBot.utilit.MessageGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@SpringBootTest

public class BotStateHandlerImplTest {

    @Autowired
    BotStateHandlerImpl botStateHandler;
    @Autowired
    ChatServiceImpl chatService;
    @Autowired
    KeyBoardServiceImpl keyBoardService;
    @Autowired
    MessageGenerator messageGenerator;

    Long chatId = 1L;

    @BeforeEach
    void addTestChat() {
        chatService.addChat(chatId, BotState.DEFAULT);
    }

    @AfterEach
    void deleteTestChat() {
        chatService.delete(chatId);
    }

    @Test
    void handleSetCityStateTest() {
        chatService.setBotState(chatId, BotState.SET_CITY);
        SendMessage sm = botStateHandler.handleBotState(chatId, "Магадан", BotState.SET_CITY);
        Assertions.assertTrue(
                sm.getText()
                        .equals(messageGenerator.generateWeatherMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.GET_WEATHER
                        && sm.getReplyMarkup().equals(keyBoardService.getWeatherButtonRow())
        );
    }

    @Test
    void handleSetDefaultCityStateTest() {
        chatService.setBotState(chatId, BotState.SET_DEFAULT_CITY);
        Assertions.assertTrue(
                botStateHandler.handleBotState(chatId, "Магадан", BotState.SET_DEFAULT_CITY).getText()
                        .equals(messageGenerator.generateDefaultCityMessage("Магадан"))
                        && chatService.getByChatId(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void handleWrongCityCaseTest() {
        chatService.setBotState(chatId, BotState.SET_CITY);
        Assertions.assertTrue(
                botStateHandler.handleBotState(chatId, "несуществующий", BotState.SET_CITY).getText().
                        equals(messageGenerator.generateNoSuchCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_CITY
        );

        chatService.setBotState(chatId, BotState.SET_DEFAULT_CITY);
        Assertions.assertTrue(
                botStateHandler.handleBotState(chatId, "несуществующий", BotState.SET_DEFAULT_CITY).getText().
                        equals(messageGenerator.generateNoSuchCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_DEFAULT_CITY
        );
    }

    @Test
    void handleNonRussianCityCaseTest() {
        chatService.setBotState(chatId, BotState.SET_CITY);
        Assertions.assertTrue(
                botStateHandler.handleBotState(chatId, "Лондон", BotState.SET_CITY).getText().
                        equals(messageGenerator.generateNonRussianCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_CITY
        );

        chatService.setBotState(chatId, BotState.SET_DEFAULT_CITY);
        Assertions.assertTrue(
                botStateHandler.handleBotState(chatId, "Лондон", BotState.SET_DEFAULT_CITY).getText().
                        equals(messageGenerator.generateNonRussianCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_DEFAULT_CITY
        );
    }
}
