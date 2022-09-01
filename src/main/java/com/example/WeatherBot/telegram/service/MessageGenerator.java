package com.example.WeatherBot.telegram.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду. Введи город, в котором хочешь узнать погоду";
    }
}
