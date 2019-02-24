package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;

@Module(includes = {ViewModelModule.class, ForecastModule.class})
public class ApplicationModule {
    private static final String ROOT_URL = "http://api.openweathermap.org";

    @Provides
    @Singleton
    ForecastDao provideForecastDao(Application application) {
        return ForecastDatabase.getDatabase(application).forecastDao();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(ROOT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static WeatherApi provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }
}
