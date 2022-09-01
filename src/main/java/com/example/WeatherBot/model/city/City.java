package com.example.WeatherBot.model.city;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Getter
public class City {

    private List<CityInfo> cityInfo;

}
