package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.DBModel.DefaultCity;
import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DefaultCityRepository extends JpaRepository<DefaultCity, Long> {
    Optional<DefaultCity> findByName(String name);
}
