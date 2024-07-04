package com.example.WeatherBot.telegram.handler;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.BotState;
import com.example.WeatherBot.service.*;
import com.example.WeatherBot.telegram.handler.CommandHandler;
import com.example.WeatherBot.utilit.MessageGenerator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@SpringBootTest
public class CommandHandlerImplTest {

    @Autowired
    CommandHandlerImpl commandHandler;
    @Autowired
    ChatServiceImpl chatService;
    @Autowired
    DBCityServiceImpl dbCityService;
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
    void handleStartCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "START").getText()
                        .equals(messageGenerator.generateStartMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void handleSetCityCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "SETCITY").getText()
                        .equals(messageGenerator.generateSetCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_CITY
        );
    }

    @Test
    void handleHelpCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "HELP").getText().
                        equals(messageGenerator.generateHelpMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void handleSetDefaultCityCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "SETDEFAULTCITY").getText()
                        .equals(messageGenerator.generateSetCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.SET_DEFAULT_CITY
        );
    }

    @Test
    void handleGetWeatherCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "GETWEATHER").getText()
                        .equals(messageGenerator.generateNoDefaultCityMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.DEFAULT
        );
    }

    @Test
    void handleGetWeatherCommandWithDefaultCityTest() {
        dbCityService.add(new DBCity("Магадан"));
        chatService.setDefaultCity(chatId, dbCityService.findByName("Магадан").get());
        SendMessage sm = commandHandler.handleCommand(chatId, "GETWEATHER");
        Assertions.assertTrue(
                sm.getText().equals(messageGenerator.generateWeatherMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.GET_WEATHER_DEFAULT
                        && sm.getReplyMarkup().equals(keyBoardService.getWeatherButtonRow())
        );
        chatService.setDefaultCity(chatId, null);
        dbCityService.delete("Магадан");
    }

    @Test
    void handleWrongCommandTest() {
        Assertions.assertTrue(
                commandHandler.handleCommand(chatId, "QWERTY").getText()
                        .equals(messageGenerator.generateNoSuchCommandMessage())
                        && chatService.getByChatId(chatId).getBotState() == BotState.DEFAULT
        );
    }
}
