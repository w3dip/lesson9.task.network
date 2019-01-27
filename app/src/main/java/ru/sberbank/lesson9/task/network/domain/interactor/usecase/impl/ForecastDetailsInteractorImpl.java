package ru.sberbank.lesson9.task.network.domain.interactor.usecase.impl;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastDetailsInteractorImpl implements ForecastDetailsInteractor {
    private ForecastRepository repository;
    private Callback<ForecastEntity> callback;
    private String date;

    public ForecastDetailsInteractorImpl(ForecastRepository repository, Callback<ForecastEntity> callback) {
        this.repository = repository;
        this.callback = callback;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void execute() {
        repository.getByDate(date, callback);
    }
}
