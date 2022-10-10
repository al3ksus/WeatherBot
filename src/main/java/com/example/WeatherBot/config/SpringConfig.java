package com.example.WeatherBot.config;

import com.example.WeatherBot.model.DBModel.Chat;
import com.example.WeatherBot.model.DBModel.DBCity;
import com.example.WeatherBot.service.ChatService;
import com.example.WeatherBot.service.DBCityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class SpringConfig {

    private final ChatService chatService;

    private final DBCityService dbCityService;

    @Scheduled(fixedDelay = 86400000)
    public void cleanDatabase() {
        Set<Long> idSet = new HashSet<>();
        List<Chat> chatList = chatService.getList();
        List<DBCity> dbCityList = dbCityService.getList();

        for (Chat chat : chatList) {
            if (chat.getDefaultCity() != null) {
                idSet.add(chat.getDefaultCity().getId());
            }

            if (chat.getTemporaryCity() != null) {
                idSet.add(chat.getTemporaryCity().getId());
            }
        }

        dbCityList.stream().filter(city -> idSet.contains(city.getId()));
        dbCityList.forEach(city -> dbCityService.delete(city.getId()));
    }
}
