package com.example.WeatherBot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    private Long id;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private BotState botState;

    public Chat(Long chatId, BotState botState) {
        this.chatId = chatId;
        this.botState = botState;
    }
}
