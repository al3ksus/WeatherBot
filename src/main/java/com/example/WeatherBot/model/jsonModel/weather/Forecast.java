package com.example.WeatherBot.model.jsonModel.weather;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class Forecast {

    private List<MainWeather> list;
}
