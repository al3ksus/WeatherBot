package com.example.WeatherBot.model.weather;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Weather {

    private String main;

    private String description;
}
