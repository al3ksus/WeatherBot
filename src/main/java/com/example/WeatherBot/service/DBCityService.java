package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.CityState;
import com.example.WeatherBot.repository.DBCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBCityService {

    @Autowired
    private DBCityRepository dbCityRepository;

    public void add(DBCity defaultCity) {
        dbCityRepository.save(defaultCity);
    }

    public Optional<DBCity> findByNameAndState(String name, CityState cityState) {
        return dbCityRepository.findByNameAndAndCityState(name, cityState);
    }
}
