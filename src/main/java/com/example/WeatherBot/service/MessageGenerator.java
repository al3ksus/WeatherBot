package com.example.WeatherBot.service;

import com.example.WeatherBot.model.weather.CurrentWeather;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

import static javax.swing.text.html.HTML.Tag.U;

@Service
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду в городах России.\nЕсли хотите узнать погоду, жмите на кнопку \"Выбрать город\".\nЧтобы увидеть список комманд используйте /help";
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

    public String generateCurrentWeatherMessage(CurrentWeather currentWeather, String city) {

        DecimalFormat decimalFormat = new DecimalFormat("#");
        return "В городе " + city
                + " " + currentWeather.getWeather().get(0).getDescription() + ",\n"
                + "температра воздуха " + decimalFormat.format(currentWeather.getMain().getTemp()) + " \u00B0С,\n"
                + "ощущается как " + decimalFormat.format(currentWeather.getMain().getFeels_like()) + " \u00B0С,\n"
                + "скорость ветра " + decimalFormat.format(currentWeather.getWind().getSpeed()) + " м/c";

    }
}
