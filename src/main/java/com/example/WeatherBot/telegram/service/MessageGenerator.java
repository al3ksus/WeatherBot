package com.example.WeatherBot.telegram.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду. Используй команду /setcity, чтобы выбрать город";
    }

    public String generateSetCityMessage() {
        return "Введите название города";
    }

    public String generateNoSuchCommandMessage() {
        return "Такой команды нет";
    }

    public String generateNoSuchCityMessage() {
        return "Проверьте правильность написания названия города";
    }
}
