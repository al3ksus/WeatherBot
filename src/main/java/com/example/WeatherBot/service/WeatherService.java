package com.example.WeatherBot.service;

import com.example.WeatherBot.weather.CurrentWeather;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
public class WeatherService {

    private final static String API_KEY = "f567de296dab0b7026ca5ad7072e46f6";

    public CurrentWeather getCurrentWeather(String city) {
        HttpURLConnection connection = null;

        StringBuilder response = new StringBuilder();

        CurrentWeather currentWeather = null;

        try {
            connection = (HttpURLConnection) new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric").openConnection();

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

    public boolean isCityExist(String city) {
        return true;
    }
}
