package ru.sberbank.lesson9.task.network.domain.repository;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;

public interface ForecastRepository {
    List<ForecastEntity> getAll();
    void save(ForecastEntity forecast);
}
