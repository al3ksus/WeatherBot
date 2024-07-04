package com.example.WeatherBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyBoardServiceImpl implements KeyBoardService {

    @Override
    public InlineKeyboardMarkup getWeatherButtonRow() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Текущую погоду");
        button1.setCallbackData("currentWeather");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Прогноз на 5 дней");
        button2.setCallbackData("forecast");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
