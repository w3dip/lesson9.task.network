package ru.sberbank.lesson9.task.network.domain.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public interface ForecastRepository {
    LiveData<List<ForecastItem>> getAll(boolean isNetworkAvailable);
    LiveData<ForecastItem> getByDate(String date);
}
