package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.Executor;

import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToItemMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastItemToEntityMapper;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.data.utils.ForecastsDownloader;
import ru.sberbank.lesson9.task.network.data.utils.Function;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.CITY;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.ID;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.LANG;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.UNITS;

public class ForecastDataRepository implements ForecastRepository {

    private static Mapper<Forecast, List<ForecastItem>> forecastItemMapper = new ForcastInfoToItemMapper();
    private static Mapper<List<ForecastItem>, List<ForecastEntity>> forecastEntityMapper = new ForecastItemToEntityMapper();
    private static final Executor executor = r -> new Thread(r).start();

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    public ForecastDataRepository(ForecastDao forecastDao) {
        this.forecastDao = forecastDao;
    }

    public ForecastDataRepository(WeatherApi weatherApi, ForecastDao forecastDao) {
        this.weatherApi = weatherApi;
        this.forecastDao = forecastDao;
    }

    @Override
    public LiveData<List<ForecastEntity>> getAll(boolean isNetworkAvailable) {
        LiveData<List<ForecastEntity>> liveData = new ForecastsDownloader<List<ForecastEntity>, List<ForecastItem>>(forecastEntityMapper, isNetworkAvailable) {
            @Override
            protected void saveCallResult(@NonNull List<ForecastEntity> items) {
                if (!items.isEmpty()) {
                    forecastDao.deleteAll();
                }
                ForecastEntity[] arr = new ForecastEntity[items.size()];
                items.toArray(arr);
                forecastDao.insertAll(arr);
            }

            @NonNull
            @Override
            protected LiveData<List<ForecastEntity>> loadFromDb() {
                return forecastDao.getAll();
            }

            @NonNull
            @Override
            protected LiveData<List<ForecastItem>> createCall() {
                LiveData<Forecast> response = weatherApi.getWeather(CITY, ID, UNITS, LANG);
                LiveData<List<ForecastItem>> result = Transformations.map(response, forecast -> forecastItemMapper.map(forecast));
                return result;
            }
        }.getAsLiveData();

        return liveData;
    }

    @Override
    public void getByDate(String date, Function<ForecastEntity> handler) {
        new AsyncTask<String, Void, ForecastEntity>() {
            @Override
            protected ForecastEntity doInBackground(String... ids) {
                return forecastDao.getByDate(ids[0]);
            }

            @Override
            protected void onPostExecute(ForecastEntity forecastEntity) {
                handler.apply(forecastEntity);
            }
        }.execute(date);
    }
}
