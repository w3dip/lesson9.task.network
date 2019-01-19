package ru.sberbank.lesson9.task.network.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForecastEntity {
    private String date;
    private String weather;
    private String weatherDesc;
    private String temp;
}
