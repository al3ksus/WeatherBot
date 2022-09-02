package com.example.WeatherBot.telegram.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду в городах России.\nИспользуй команду /setcity, чтобы выбрать город";
    }

    public String generateSetCityMessage() {
        return "Введите название города";
    }

    public String generateNoSuchCommandMessage() {
        return "Такой команды я не знаю";
    }

    public String generateNoSuchCityMessage() {
        return "Такого города я не знаю";
    }

    public String generateNonRussianCityMessage() {
        return "Я могу показывать погоду только в городах России";
    }
}
