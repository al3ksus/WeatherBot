package com.example.WeatherBot.model.jsonModel.city;

import com.example.WeatherBot.model.jsonModel.city.LocalNames;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Embeddable
@Getter
public class City {

    @Transient
    protected LocalNames local_names;

    protected double lat;

    protected double lon;

    @Transient
    protected String country;
}
