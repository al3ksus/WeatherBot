package com.example.WeatherBot.model.jsonModel.city;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Getter
public class LocalNames {

    private String ru;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalNames that = (LocalNames) o;
        return Objects.equals(ru, that.ru);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ru);
    }
}
