package ru.sberbank.lesson9.task.network.domain.interactor.usecase;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.interactor.Interactor;

public interface ForecastGetListInteractor extends Interactor {
    /*interface Callback {
        void onForecastsLoaded(LiveData<List<ForecastEntity>> forecasts);
    }*/
}
