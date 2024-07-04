package com.example.WeatherBot.service;

import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
import com.example.WeatherBot.utilit.Link;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final static String API_KEY = "f567de296dab0b7026ca5ad7072e46f6";

    @Override
    public MainWeather getCurrentWeather(double lat, double lon) {
        String response;

        try {
            response = getURLResponse(String.format(Link.currentWeatherLink, lat, lon, API_KEY ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(String.valueOf(response), MainWeather.class);
    }

    @Override
    public City[] getCityList(String cityName) {
        String response;

        try {
            response = getURLResponse(String.format(Link.geocodingLink, cityName, API_KEY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(String.valueOf(response), City[].class);
    }

    @Override
    public Forecast getForecast(double lat, double lon) {
        String response;

        try {
            response = getURLResponse(String.format(Link.forecastLink, lat, lon, API_KEY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(String.valueOf(response), Forecast.class);
    }

    private static String getURLResponse(String urlAddress) throws IOException {
        HttpURLConnection connection;
        StringBuilder response = new StringBuilder();

        connection = (HttpURLConnection) new URL(urlAddress).openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        return String.valueOf(response);
    }
}
