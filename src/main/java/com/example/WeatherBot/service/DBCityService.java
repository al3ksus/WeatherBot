package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.model.DBModel.DBCity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface DBCityService {
    void add(DBCity defaultCity);
    Optional<DBCity> findByName(String name);
    List<DBCity> getList();
    List<DBCity> getUnnecessaryCities(List<Chat> chatList);
    void delete(String name);
}
