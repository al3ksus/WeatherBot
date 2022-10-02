package com.example.WeatherBot.service;

import com.example.WeatherBot.model.jsonModel.city.City;
import com.example.WeatherBot.model.jsonModel.weather.Forecast;
import com.example.WeatherBot.model.jsonModel.weather.MainWeather;
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

    public MainWeather getCurrentWeather(double lat, double lon) {
        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        MainWeather mainWeather;

        try {
            connection = (HttpURLConnection) new URL( String.format(Link.currentWeatherLink, lat, lon, API_KEY)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String  line;

            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            mainWeather = gson.fromJson(String.valueOf(response), MainWeather.class);

        } catch (Throwable cause) {
            return null;
        }

        return mainWeather;
    }

    public City[] isCityExist(String cityName) {

        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        City[] city;

        try {
            connection = (HttpURLConnection) new URL( String.format(Link.geocodingLink, cityName, API_KEY)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            city = new Gson().fromJson(String.valueOf(response), City[].class);

        } catch (Throwable cause) {
            return null;
        }
        return city;
    }

    public Forecast getForecast(double lat, double lon) {
        HttpURLConnection connection;

        StringBuilder response = new StringBuilder();

        Forecast forecast;

        try {
            connection = (HttpURLConnection) new URL( String.format(Link.forecastLink, lat, lon, API_KEY)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String  line;

            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            Gson gson = new Gson();
            forecast = gson.fromJson(String.valueOf(response), Forecast.class);

        } catch (Throwable cause) {
            return null;
        }

        return forecast;
    }

}
