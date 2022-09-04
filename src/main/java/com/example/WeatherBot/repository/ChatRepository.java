package com.example.WeatherBot.repository;

import com.example.WeatherBot.model.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    boolean existsByChatId(Long chatId);

    Chat getByChatId(Long chatId);
}
