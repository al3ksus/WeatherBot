package com.example.WeatherBot.utilit;

import org.springframework.stereotype.Component;

@Component
public class Link {

    public final static String currentWeatherLink = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&lang=ru&appid=%s&units=metric";
    public final static String geocodingLink = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=%s&units=metric";

    public final static String forecastLink = "https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&lang=ru&appid=%s&units=metric";
}
