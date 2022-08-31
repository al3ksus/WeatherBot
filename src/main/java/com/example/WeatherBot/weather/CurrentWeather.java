package com.example.WeatherBot.weather;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class CurrentWeather {

    private List<Weather> weather;

    private Main main;
}
