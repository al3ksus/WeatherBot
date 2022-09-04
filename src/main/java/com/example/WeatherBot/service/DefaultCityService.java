package com.example.WeatherBot.service;

import com.example.WeatherBot.model.chat.DefaultCity;
import com.example.WeatherBot.repository.DefaultCityRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DefaultCityService {

    @Autowired
    private DefaultCityRepository defaultCityRepository;

    public void addDefaultCity(String name, double lat, double lon) {
        defaultCityRepository.save(new DefaultCity(name, lat, lon));
    }

    public void addDefaultCity(DefaultCity defaultCity) {
        defaultCityRepository.save(defaultCity);
    }
}
