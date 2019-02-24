package ru.sberbank.lesson9.task.network.presentation.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastGetListInteractorImpl;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class ForecastViewModel extends AndroidViewModel implements Callback<LiveData<List<ForecastItem>>> {
    private LiveData<List<ForecastItem>> forecasts;

    @Inject
    public ForecastViewModel(Application application, ForecastRepository repository) {
        super(application);
        ForecastGetListInteractorImpl interactor = new ForecastGetListInteractorImpl(repository, this);
        interactor.setNetworkAvailable(checkConnection(application.getApplicationContext()));
        interactor.execute();
    }

    public LiveData<List<ForecastItem>> getForecasts() {
        return forecasts;
    }

    @Override
    public void handle(LiveData<List<ForecastItem>> forecasts) {
        this.forecasts = forecasts;
    }
}
