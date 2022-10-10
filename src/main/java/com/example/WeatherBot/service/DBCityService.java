package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.CityState;
import com.example.WeatherBot.repository.DBCityRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DBCityService {

    private final DBCityRepository dbCityRepository;

    public void add(DBCity defaultCity) {
        dbCityRepository.save(defaultCity);
    }

    public Optional<DBCity> findByNameAndState(String name, CityState cityState) {
        return dbCityRepository.findByNameAndAndCityState(name, cityState);
    }

    public List<DBCity> getList() {
        return dbCityRepository.findAll();
    }

    public void delete(Long id) {
        dbCityRepository.deleteById(id);
    }
}
