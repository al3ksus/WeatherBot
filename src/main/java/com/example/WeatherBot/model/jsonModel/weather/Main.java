package com.example.WeatherBot.model.jsonModel.weather;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Main {

    private double temp;

    private double feels_like;
}
