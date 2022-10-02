package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DefaultCity;
import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import com.example.WeatherBot.repository.DefaultCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultCityService {

    @Autowired
    private DefaultCityRepository defaultCityRepository;

    public void add(DefaultCity defaultCity) {
        defaultCityRepository.save(defaultCity);
    }

    public Optional<DefaultCity> findByName(String name) {
        return defaultCityRepository.findByName(name);
    }
}
