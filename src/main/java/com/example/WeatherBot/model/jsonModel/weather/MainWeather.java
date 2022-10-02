package com.example.WeatherBot.model.jsonModel.weather;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class MainWeather {

    private List<Weather> weather;

    private Main main;

    private Wind wind;

    private Long dt;
}
