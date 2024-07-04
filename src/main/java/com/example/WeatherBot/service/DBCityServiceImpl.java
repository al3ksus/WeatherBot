package com.example.WeatherBot.service;

import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.repository.DBCityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DBCityServiceImpl implements DBCityService {

    private final DBCityRepository dbCityRepository;

    @Override
    public void add(DBCity defaultCity) {
        dbCityRepository.save(defaultCity);
    }

    @Override
    public Optional<DBCity> findByName(String name) {
        return dbCityRepository.findByName(name);
    }

    @Override
    public List<DBCity> getList() {
        return dbCityRepository.findAll();
    }

    @Override
    public List<DBCity> getUnnecessaryCities(List<Chat> chatList) {
        List<DBCity> dbCityList = dbCityRepository.findAll();
        List<Long> idList = new ArrayList<>(
                chatList.stream().map(
                        c -> c.getTemporaryCity() == null? null : c.getTemporaryCity().getId())
                        .toList());
        idList.addAll(chatList.stream().map(
                c -> c.getDefaultCity() == null? null : c.getDefaultCity().getId())
                .toList());

        return dbCityList.stream().filter(c -> !idList.contains(c.getId())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String name) {
        dbCityRepository.deleteByName(name);
    }
}
