package com.example.WeatherBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface KeyBoardService {
    InlineKeyboardMarkup getWeatherButtonRow();
}
