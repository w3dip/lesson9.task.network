package ru.sberbank.lesson9.task.network.data.repository;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastDataRepository implements ForecastRepository {
    @Override
    public List<ForecastEntity> getAll() {
        return null;
    }

    @Override
    public void save(ForecastEntity forecast) {

    }
}
