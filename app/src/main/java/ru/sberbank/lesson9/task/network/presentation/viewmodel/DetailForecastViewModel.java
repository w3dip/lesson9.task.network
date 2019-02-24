package ru.sberbank.lesson9.task.network.presentation.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import javax.inject.Inject;

import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class DetailForecastViewModel extends AndroidViewModel implements Callback<LiveData<ForecastItem>> {
    private LiveData<ForecastItem> forecast;
    private ForecastRepository repository;

    @Inject
    public DetailForecastViewModel(Application application, ForecastRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void getDetailedForecast(String date) {
        ForecastDetailsInteractor forecastDetailsInteractor = new ForecastDetailsInteractor(repository, this);
        forecastDetailsInteractor.setDate(date);
        forecastDetailsInteractor.execute();
    }

    public LiveData<ForecastItem> getForecast() {
        return forecast;
    }

    @Override
    public void handle(LiveData<ForecastItem> forecast) {
        this.forecast = forecast;
    }
}
