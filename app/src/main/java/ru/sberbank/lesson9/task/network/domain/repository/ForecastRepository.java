package ru.sberbank.lesson9.task.network.domain.repository;

import android.arch.lifecycle.LiveData;


import java.util.List;

import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;

public interface ForecastRepository {
    LiveData<List<ForecastEntity>> getAll(boolean isNetworkAvailable);
    void getByDate(String date, Callback<ForecastEntity> callback);
}
