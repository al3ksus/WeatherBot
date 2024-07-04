package com.example.WeatherBot.config;

import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.DBCityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class SpringConfig {

    private final ChatService chatService;

    private final DBCityService dbCityService;

    @Scheduled(fixedDelay = 86400000)
    public void cleanDatabase() {
        List<DBCity> unnecessaryCities = dbCityService.getUnnecessaryCities(chatService.getList());
        unnecessaryCities.forEach(c -> dbCityService.delete(c.getName()));
    }
}
