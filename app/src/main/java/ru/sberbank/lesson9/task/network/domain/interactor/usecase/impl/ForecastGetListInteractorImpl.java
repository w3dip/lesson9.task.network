package ru.sberbank.lesson9.task.network.domain.interactor.usecase.impl;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastGetListInteractor;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastGetListInteractorImpl implements ForecastGetListInteractor {
    private Callback<LiveData<List<ForecastEntity>>> callback;
    private ForecastRepository repository;
    private boolean isNetworkAvailable;

    public ForecastGetListInteractorImpl(ForecastRepository repository, Callback<LiveData<List<ForecastEntity>>> callback) {
        this.repository = repository;
        this.callback = callback;
    }

    public void setNetworkAvailable(boolean networkAvailable) {
        isNetworkAvailable = networkAvailable;
    }

    @Override
    public void execute() {
        callback.handle(repository.getAll(isNetworkAvailable));
    }
}
