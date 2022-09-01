package com.example.WeatherBot.service;

import com.example.WeatherBot.model.city.City;
import com.example.WeatherBot.model.weather.CurrentWeather;
import com.example.WeatherBot.utilit.Link;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {

    private final static String API_KEY = "f567de296dab0b7026ca5ad7072e46f6";

    public CurrentWeather getCurrentWeather(double lat, double lon) {
        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        CurrentWeather currentWeather;

        try {
            connection = (HttpURLConnection) new URL( Link.currentWeatherLink + String.valueOf(lat) + "&lon=" + String.valueOf(lon) + "&appid=" + API_KEY + "&units=metric").openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String  line;

            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            currentWeather = gson.fromJson(String.valueOf(response), CurrentWeather.class);

        } catch (Throwable cause) {
            return null;
        }

        return currentWeather;
    }

    public City isCityExist(String cityTitle) {
        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        City city;

        try {
            connection = (HttpURLConnection) new URL( Link.geocodingLink + cityTitle + "&appid=" + API_KEY + "&units=metric").openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String  line;

            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            city = gson.fromJson(String.valueOf(response), City.class);

        } catch (Throwable cause) {
            return null;
        }

        return city;
    }

}
