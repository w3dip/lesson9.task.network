package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import ru.sberbank.lesson9.task.network.data.repository.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDatabase;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.presentation.view.activity.BaseView;

public class DetailForecastViewModel extends AndroidViewModel {
    private ForecastRepository repository;

    public DetailForecastViewModel(Application application) {
        super(application);
        ForecastDatabase forecastDatabase = ForecastDatabase.getDatabase(application);
        ForecastDao forecastDao = forecastDatabase.forecastDao();
        repository = new ForecastDataRepository(forecastDao);
    }

    public void getDetailedForecast(String date, BaseView view) {
        repository.getByDate(date, (forecast)->
            view.handle(forecast)
        );
    }
}
