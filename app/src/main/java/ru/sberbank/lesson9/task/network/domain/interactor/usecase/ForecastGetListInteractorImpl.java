package ru.sberbank.lesson9.task.network.domain.interactor.usecase;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.Interactor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastGetListInteractorImpl implements Interactor {
    private Callback<LiveData<List<ForecastItem>>> callback;
    private ForecastRepository repository;
    private boolean isNetworkAvailable;

    public ForecastGetListInteractorImpl(ForecastRepository repository, Callback<LiveData<List<ForecastItem>>> callback) {
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
