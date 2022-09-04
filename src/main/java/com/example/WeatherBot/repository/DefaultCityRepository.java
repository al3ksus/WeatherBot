package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.chat.DefaultCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultCityRepository extends JpaRepository<DefaultCity, Long> {

}
