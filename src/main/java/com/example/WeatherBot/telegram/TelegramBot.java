package com.example.WeatherBot.telegram;

import com.example.WeatherBot.config.BotConfig;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.BotService;
import com.example.WeatherBot.service.WeatherService;
import com.example.WeatherBot.service.MessageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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

        try
        {
            execute(botService.handleUpdate(update));
        } catch (TelegramApiException e) {
            try {
                execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Чтобы увидеть список комманд используйте /help"));
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*public void sendMessage (Long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageWithButton(Long chatId, String text) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Выбрать город");
        button.setCallbackData("button");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);
        SendMessage send = new SendMessage(String.valueOf(chatId), text);
        inlineKeyboardMarkup.setKeyboard(rowList);
        send.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(send);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public SendMessage handleUpdate(Update update) {
        String messageText = "";
        Long chatId = null;

        if (update.hasMessage()) {
            messageText = update.getMessage().getText().toUpperCase();
            chatId = update.getMessage().getChatId();
        }

        if (!chatService.isChatExist(chatId)) {
            chatService.addChat(chatId, BotState.DEFAULT);
            return new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());//sendMessage(update.getMessage().getChatId(), messageGenerator.generateStartMessage());
        } else {
            return handleBotState(update, messageText, chatId);
        }
    }

    public SendMessage handleBotState(Update update, String messageText, Long chatId) {

        if (messageText.charAt(0) == '/') {

            String command = messageText.substring(1);

            switch (command) {
                case "START" -> {
                    chatService.setBotState(chatId, BotState.DEFAULT);
                    return new SendMessage(String.valueOf(chatId), messageGenerator.generateStartMessage());//sendMessageWithButton(chatId, messageGenerator.generateStartMessage());
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
                    CityInfo[] cityInfo = weatherService.isCityExist(messageText);

                    if (cityInfo.length != 0) {
                        Optional<CityInfo> optionalCityInfo = Arrays.stream(cityInfo).filter(info -> info.getCountry().equals("RU")).findFirst();

                        if (optionalCityInfo.isPresent()) {

                            CurrentWeather currentWeather = weatherService.getCurrentWeather(optionalCityInfo.get().getLat(), optionalCityInfo.get().getLon());
                            String cityName = messageText.charAt(0) + messageText.substring(1).toLowerCase();

                            sendMessage(chatId, messageGenerator.generateCurrentWeatherMessage(currentWeather, cityName));

                        } else {
                            sendMessage(chatId, messageGenerator.generateNonRussianCityMessage());
                        }
                    } else {
                        sendMessage(chatId, messageGenerator.generateNoSuchCityMessage());
                    }

                }
            }
        }

        return  null;
    }*/

}
