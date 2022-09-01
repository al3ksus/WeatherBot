package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    public boolean existsByChatId(Long chatId);
}
