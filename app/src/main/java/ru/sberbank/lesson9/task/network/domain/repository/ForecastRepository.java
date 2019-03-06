package ru.sberbank.lesson9.task.network.domain.repository;

import java.util.List;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public interface ForecastRepository {
    Single<List<ForecastItem>> getAll(boolean isNetworkAvailable);
    Single<ForecastItem> getByDate(String date);
    Single<List<Long>> persist(List<ForecastItem> items);
}
