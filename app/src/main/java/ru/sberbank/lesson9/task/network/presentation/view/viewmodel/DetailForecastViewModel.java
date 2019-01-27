package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class DetailForecastViewModel extends AndroidViewModel implements Callback<LiveData<ForecastItem>> {
    private LiveData<ForecastItem> forecast;
    private ForecastRepository repository;

    public DetailForecastViewModel(Application application) {
        super(application);
        ForecastDatabase forecastDatabase = ForecastDatabase.getDatabase(application);
        ForecastDao forecastDao = forecastDatabase.forecastDao();
        repository = new ForecastDataRepository(forecastDao);
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
