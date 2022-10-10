package com.example.WeatherBot.model.DBModel;

import com.example.WeatherBot.model.enums.CityState;
import com.example.WeatherBot.model.jsonModel.city.City;
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
public class DBCity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Embedded
    private City city;

    private String name;

    @Enumerated(EnumType.STRING)
    private CityState cityState;

    public DBCity(City city, CityState cityState) {
        this.city = city;
        this.cityState = cityState;
        name = city.getLocal_names().getRu();
    }
}

