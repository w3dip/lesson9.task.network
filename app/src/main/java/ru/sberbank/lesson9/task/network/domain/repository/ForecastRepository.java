package ru.sberbank.lesson9.task.network.domain.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public interface ForecastRepository {
    LiveData<List<ForecastItem>> getAll(boolean isNetworkAvailable);
    Single<ForecastItem> getByDate(String date);
}
