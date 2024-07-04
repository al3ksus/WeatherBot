package com.example.WeatherBot.config;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.service.ChatServiceImpl;
import com.example.WeatherBot.service.DBCityServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class SpringConfig {

    private final ChatServiceImpl chatServiceImpl;

    private final DBCityServiceImpl dbCityServiceImpl;

    @Scheduled(fixedDelay = 86400000)
    public void cleanDatabase() {
        List<DBCity> unnecessaryCities = dbCityServiceImpl.getUnnecessaryCities(chatServiceImpl.getList());
        unnecessaryCities.forEach(c -> dbCityServiceImpl.delete(c.getName()));
    }
}
