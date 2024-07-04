package com.example.WeatherBot.service;

import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.example.WeatherBot.utilit.Link;
import com.google.gson.Gson;

import java.io.IOException;

public interface WeatherService {
    MainWeather getCurrentWeather(double lat, double lon);
    City[] getCityList(String cityName);
    Forecast getForecast(double lat, double lon);
}
