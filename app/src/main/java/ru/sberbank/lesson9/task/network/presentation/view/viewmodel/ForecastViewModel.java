package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.impl.ForecastGetListInteractorImpl;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class ForecastViewModel extends AndroidViewModel implements Callback<LiveData<List<ForecastEntity>>> {
    private LiveData<List<ForecastEntity>> forecasts;

    public ForecastViewModel(Application application) {
        super(application);
        ForecastDao forecastDao = ForecastDatabase.getDatabase(application).forecastDao();
        WeatherApi weatherApi = WeatherApiClient.getApiClient();
        ForecastRepository repository = new ForecastDataRepository(weatherApi, forecastDao);

        ForecastGetListInteractorImpl interactor = new ForecastGetListInteractorImpl(repository, this);
        interactor.setNetworkAvailable(checkConnection(application.getApplicationContext()));
        interactor.execute();
    }

    public LiveData<List<ForecastEntity>> getForecasts() {
        return forecasts;
    }

    @Override
    public void handle(LiveData<List<ForecastEntity>> forecasts) {
        this.forecasts = forecasts;
    }
}
