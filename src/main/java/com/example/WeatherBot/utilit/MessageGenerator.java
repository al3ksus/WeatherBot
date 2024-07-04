package com.example.WeatherBot.utilit;

import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Calendar;


@Component
public class MessageGenerator {

    public String generateStartMessage() {
        return "Привет, я умею показывать погоду в городах России.\n" +
                "Чтобы увидеть инструкцию используйте /help.";
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

    public String generateGetInstructionMessage() {
        return "Чтобы увидеть список комманд используйте /help";
    }

    public String generateCurrentWeatherMessage(MainWeather mainWeather, String city) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(mainWeather.getDt()*1000);
        int hour = date.get(Calendar.HOUR_OF_DAY);

        DecimalFormat decimalFormat = new DecimalFormat("#");
        return "В городе " + city
                + " " + mainWeather.getWeather().get(0).getDescription() + " "
                + getWeatherEmoji(mainWeather.getWeather().get(0).getDescription(), hour) +",\n"
                + "температра воздуха " + decimalFormat.format(mainWeather.getMain().getTemp()) + " \u00B0С,\n"
                + "ощущается как " + decimalFormat.format(mainWeather.getMain().getFeels_like()) + " \u00B0С,\n"
                + "скорость ветра " + decimalFormat.format(mainWeather.getWind().getSpeed()) + " м/c";

    }

    public String generateDefaultCityMessage(String cityName) {
        return "Новый город по умолчанию - " + cityName + ".";
    }

    public String generateNoDefaultCityMessage() {
        return "Эта команда показывает погоду в городе по умолчанию." +
                "Чтобы выбрать город по умолчанию используй команду /setdefaultcity";
    }

    public String generateWeatherMessage() {
        return "Что именно вы хотите узнать?";
    }

    public String generateForecastMessage(Forecast forecast, String cityName) {
        Calendar date = Calendar.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat("#");
        int prevDay = -1;
        int day;
        int month;
        int hour;
        StringBuilder forecastStr = new StringBuilder();
        forecastStr.append(cityName)
                .append("\nВремя    описание    температура    ветер\n");

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

            hour = date.get(Calendar.HOUR_OF_DAY);
            forecastStr.append(hour < 10? "0" + hour: hour)
                    .append(":00    ")
                    .append(getWeatherEmoji(weather.getWeather().get(0).getDescription(), hour))
                    .append("    ")
                    .append(decimalFormat.format(weather.getMain().getTemp()))
                    .append(" \u00B0С    ")
                    .append(decimalFormat.format(weather.getWind().getSpeed()))
                    .append(" м/с\n");

            prevDay = day;
        }
        return String.valueOf(forecastStr);
    }

    public String generateHelpMessage() {
        return """
                Я могу показывать погоду в городах России. Ты можешь
                узнать текущую погоду или прогноз на 5 дней с шагом
                в 3 часа. Если есть город, в котором тебе нужно узнавать
                погоду чаще, чем в остальных, можешь выбрать город по
                умолчанию и потом, с помощью одной команды сможешь
                узнавать погоду в этом городе.
                                
                Команды:
                /start    инициализация чата, это можно сделать один раз в начале использования
                /help    получить подробную информацию о боте
                /setdefaultcity    выбрать новый город по умолчанию
                /setcity    выбрать город, чтобы узнать в нем погоду
                /getweather    узнать погоду в городе по умолчанию
                                
                по поводу вопросов и предложений пишите на почту
                aleksejukrainskij7554@gmail.com
                """;
    }

    private String getWeatherEmoji(String description, int hour) {
        return switch (description) {
            case "ясно" -> hour == 21 || hour <= 3? EmojiParser.parseToUnicode(":new_moon:"):EmojiParser.parseToUnicode(":sunny:");
            case "пасмурно" -> EmojiParser.parseToUnicode(":cloud:");
            case "небольшая облачность" -> hour == 21 || hour <= 3? EmojiParser.parseToUnicode(":cloud:"): EmojiParser.parseToUnicode("\uD83C\uDF24");
            case "облачно с прояснениями" -> hour == 21 || hour <= 3? EmojiParser.parseToUnicode(":cloud:"): EmojiParser.parseToUnicode(":white_sun_behind_cloud:");
            case "небольшой дождь" -> hour == 21 || hour <= 3? EmojiParser.parseToUnicode("\uD83C\uDF27"): EmojiParser.parseToUnicode("\uD83C\uDF26");
            case "переменная облачность" -> hour == 21 || hour <= 3? EmojiParser.parseToUnicode(":cloud:"): EmojiParser.parseToUnicode(":partly_sunny:");
            case "дождь" -> EmojiParser.parseToUnicode("\uD83C\uDF27");
            default -> "";
        };
    }
}
