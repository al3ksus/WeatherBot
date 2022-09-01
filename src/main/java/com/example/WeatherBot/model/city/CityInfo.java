package com.example.WeatherBot.model.city;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CityInfo {

    private LocalNames localNames;

    private double lat;

    private double lon;

    private String country;
}
