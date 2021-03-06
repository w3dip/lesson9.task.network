package ru.sberbank.lesson9.task.network.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Builder;
import lombok.Data;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.TABLE_NAME;

@Data
@Builder
@Entity(tableName = TABLE_NAME)
public class ForecastEntity {
    public static final String TABLE_NAME = "forecasts";
    public static final String COLUMN_DATE = "date";
    public static final String FORECAST_DATE = "ru.sberbank.lesson9.task.network.domain.entity.FORECAST_DATE";

    @PrimaryKey
    @NonNull
    private String date;
    private String weather;
    private String weatherDesc;
    private String temp;
    private String wind;
    private String humidity;
    private String pressure;

    public ForecastEntity(String date, String weather, String weatherDesc, String temp, String wind, String humidity, String pressure) {
        this.date = date;
        this.weather = weather;
        this.weatherDesc = weatherDesc;
        this.temp = temp;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}
