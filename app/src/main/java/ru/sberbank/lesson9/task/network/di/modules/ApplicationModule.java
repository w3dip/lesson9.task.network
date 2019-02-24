package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;

@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    @Provides
    @Singleton
    ForecastDao provideForecastDao(Application application) {
        return ForecastDatabase.getDatabase(application).forecastDao();
    }

    @Provides
    @Singleton
    WeatherApi provideWeatherApi() {
        return WeatherApiClient.getApiClient();
    }
}
