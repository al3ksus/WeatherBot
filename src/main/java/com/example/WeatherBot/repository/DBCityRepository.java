package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.model.enums.CityState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DBCityRepository extends JpaRepository<DBCity, Long> {
    Optional<DBCity> findByNameAndAndCityState(String name, CityState cityState);
}
