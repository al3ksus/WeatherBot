package com.example.WeatherBot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private BotState botState;

    public Chat(Long chatId, BotState botState) {
        this.chatId = chatId;
        this.botState = botState;
    }
}
