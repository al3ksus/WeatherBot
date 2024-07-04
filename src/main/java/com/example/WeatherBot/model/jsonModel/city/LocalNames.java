package com.example.WeatherBot.model.jsonModel.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class LocalNames {

    private final String ru;

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
