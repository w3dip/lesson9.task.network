package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import ru.sberbank.lesson9.task.network.data.repository.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class ForecastViewModel extends AndroidViewModel {
    private LiveData<List<ForecastEntity>> forecasts;

    public ForecastViewModel(Application application) {
        super(application);
        ForecastDatabase forecastDatabase = ForecastDatabase.getDatabase(application);
        ForecastDao forecastDao = forecastDatabase.forecastDao();
        WeatherApi weatherApi = WeatherApiClient.getApiClient();
        ForecastRepository repository = new ForecastDataRepository(weatherApi, forecastDao);
        boolean isNetworkAvailable = checkConnection(application.getApplicationContext());
        forecasts = repository.getAll(isNetworkAvailable);
    }

    public LiveData<List<ForecastEntity>> getForecasts() {
        return forecasts;
    }
}
