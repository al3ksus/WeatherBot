package com.example.WeatherBot.model.DBModel;

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

    private String name;

    private double lat;

    private double lon;

    public DBCity(City city) {
        lat = city.getLat();
        lon = city.getLon();
        name = city.getLocal_names().getRu();
    }

    public DBCity(String name) {
        this.name = name;
    }
}

