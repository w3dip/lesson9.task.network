package ru.sberbank.lesson9.task.network.presentation.view.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.presentation.view.BaseView;

public class DetailForecastViewModel extends AndroidViewModel {
    private ForecastRepository repository;

    public DetailForecastViewModel(Application application) {
        super(application);
        ForecastDatabase forecastDatabase = ForecastDatabase.getDatabase(application);
        ForecastDao forecastDao = forecastDatabase.forecastDao();
        repository = new ForecastDataRepository(forecastDao);
    }

    public void getDetailedForecast(String date, BaseView view) {
        ForecastDetailsInteractor forecastDetailsInteractor = new ForecastDetailsInteractor(repository, (forecast) ->
                view.handle(forecast)
        );
        forecastDetailsInteractor.setDate(date);
        forecastDetailsInteractor.execute();
    }
}
