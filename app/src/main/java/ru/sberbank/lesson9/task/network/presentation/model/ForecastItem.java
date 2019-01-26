package ru.sberbank.lesson9.task.network.presentation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForecastItem {
    private String date;
    private String weather;
    private String weatherDesc;
    private String temp;
    private String wind;
    private String humidity;
    private String pressure;
}
