package ru.sberbank.lesson9.task.network.domain.interactor.usecase;

import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.Interactor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastDetailsInteractor implements Interactor {
    private ForecastRepository repository;
    private Callback<ForecastItem> callback;
    private String date;

    public ForecastDetailsInteractor(ForecastRepository repository, Callback<ForecastItem> callback) {
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
