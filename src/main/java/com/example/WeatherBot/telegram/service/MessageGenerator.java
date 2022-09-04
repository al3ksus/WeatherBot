package com.example.WeatherBot.telegram.service;

import com.example.WeatherBot.model.weather.Forecast;
import com.example.WeatherBot.model.weather.MainWeather;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Calendar;


@Service
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду в городах России.\n" +
                "Чтобы увидеть инструкцию используйте /help.\n" +
                "Для начала давайте выберем город по умолчанию";
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

    public String generateCurrentWeatherMessage(MainWeather mainWeather, String city) {

        DecimalFormat decimalFormat = new DecimalFormat("#");
        return "В городе " + city
                + " " + mainWeather.getWeather().get(0).getDescription() + ",\n"
                + "температра воздуха " + decimalFormat.format(mainWeather.getMain().getTemp()) + " \u00B0С,\n"
                + "ощущается как " + decimalFormat.format(mainWeather.getMain().getFeels_like()) + " \u00B0С,\n"
                + "скорость ветра " + decimalFormat.format(mainWeather.getWind().getSpeed()) + " м/c";

    }

    public String generateDefaultCityMessage(String cityName) {
        return "Новый город по умолчанию - " + cityName + ".\n"
                + "Чтобы узнать погоду в городе по умолчанию, используйте команду /getweather."
                + " Если хотите изменить город по умолчанию, используйте команду /setdefaultcity."
                + " Используйте команду /setcity, чтобы узнать погоду в другом городе";
    }

    public String generateNoDefaultCityMessage() {
        return "Эта команда показывает погоду в городе по умолчанию";
    }

    public String generateWeatherMessage() {
        return "Что именно вы хотите узнать?";
    }

    public String generateForecastMessage(Forecast forecast) {
        Calendar date = Calendar.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat("#");
        int prevDay = -1;
        int day;
        int month;
        StringBuilder forecastStr = new StringBuilder();
        forecastStr.append("Время    описание    температура    ветер\n");

        for(MainWeather weather : forecast.getList()) {
            date.setTimeInMillis(weather.getDt()*1000);
            day = date.get(Calendar.DAY_OF_MONTH);

            if (day != prevDay) {
                month = date.get(Calendar.MONTH) + 1;
                forecastStr.append("\n")
                        .append(day)
                        .append(".")
                        .append(month < 10? "0" + month: month)
                        .append("\n\n");
            }

            forecastStr.append(date.get(Calendar.HOUR_OF_DAY))
                    .append(":00    ")
                    .append(weather.getWeather().get(0).getDescription())
                    .append("    ")
                    .append(decimalFormat.format(weather.getMain().getTemp()))
                    .append(" \u00B0С    ")
                    .append(decimalFormat.format(weather.getWind().getSpeed()))
                    .append(" м/с\n");

            prevDay = day;
        }
        return String.valueOf(forecastStr);
    }
}
