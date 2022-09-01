package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    boolean existsByChatId(Long chatId);

    Chat getByChatId(Long chatId);
}
