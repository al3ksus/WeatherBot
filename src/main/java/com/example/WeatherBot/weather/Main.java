package com.example.WeatherBot.weather;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Main {

    private double temp;

    private double feelsLike;
}
