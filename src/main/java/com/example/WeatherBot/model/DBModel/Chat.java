package com.example.WeatherBot.model.DBModel;

import com.example.WeatherBot.model.enums.BotState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private BotState botState;

    @OneToOne
    private DefaultCity defaultCity;

    public Chat(Long chatId, BotState botState) {
        this.chatId = chatId;
        this.botState = botState;
    }
}
