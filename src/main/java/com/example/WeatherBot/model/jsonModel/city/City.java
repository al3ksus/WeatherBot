package com.example.WeatherBot.model.jsonModel.city;

import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
