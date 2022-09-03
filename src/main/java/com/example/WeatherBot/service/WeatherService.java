package com.example.WeatherBot.service;

import com.example.WeatherBot.model.city.CityInfo;
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
            connection = (HttpURLConnection) new URL( Link.currentWeatherLink + lat + "&lon=" + lon + "&lang=ru&appid=" + API_KEY + "&units=metric").openConnection();

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

    public CityInfo[] isCityExist(String cityTitle) {

        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        CityInfo[] cityInfo;

        try {
            connection = (HttpURLConnection) new URL(Link.geocodingLink + cityTitle + "&limit=10&appid=" + API_KEY + "&units=metric").openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            cityInfo = new Gson().fromJson(String.valueOf(response), CityInfo[].class);

        } catch (Throwable cause) {
            return null;
        }
        return cityInfo;
    }

}
