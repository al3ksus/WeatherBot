package com.example.WeatherBot.model.jsonModel.city;

import lombok.Getter;

import javax.persistence.*;

@Getter
public class City {

    @Transient
    private LocalNames local_names;

    private double lat;

    private double lon;

    @Transient
    private String country;

    public void setLocal_names(LocalNames local_names) {
        this.local_names = local_names;
    }
}
