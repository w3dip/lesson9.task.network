package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

//@Singleton
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
    /*private static final String BASE_URL = "https://api.github.com/";

    @Singleton
    @Provides
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }*/

    /*@Singleton
    @Provides
    static RepoService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(RepoService.class);
    }*/
}
