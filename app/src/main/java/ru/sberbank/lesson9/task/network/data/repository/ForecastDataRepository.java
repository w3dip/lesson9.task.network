package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import java.util.List;

import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToItemMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastItemToEntityMapper;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
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

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    private final MediatorLiveData<List<ForecastEntity>> result = new MediatorLiveData<>();

    public ForecastDataRepository(ForecastDao forecastDao) {
        this.forecastDao = forecastDao;
    }

    public ForecastDataRepository(WeatherApi weatherApi, ForecastDao forecastDao) {
        this.weatherApi = weatherApi;
        this.forecastDao = forecastDao;
    }

    @Override
    public LiveData<List<ForecastEntity>> getAll(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            final LiveData<List<ForecastItem>> apiResponse = Transformations
                    .map(weatherApi.getWeather(CITY, ID, UNITS, LANG),
                            forecast -> forecastItemMapper.map(forecast)
                    );
            result.addSource(apiResponse, response -> {
                saveResultAndReInit(forecastEntityMapper.map(response));
            });
        } else {
            result.addSource(loadFromDb(), newData -> result.setValue(newData));
        }
        return result;
    }

    @Override
    public void getByDate(String date, Callback<ForecastEntity> callback) {
        new AsyncTask<String, Void, ForecastEntity>() {
            @Override
            protected ForecastEntity doInBackground(String... ids) {
                return forecastDao.getByDate(ids[0]);
            }

            @Override
            protected void onPostExecute(ForecastEntity forecastEntity) {
                callback.handle(forecastEntity);
            }
        }.execute(date);
    }

    private LiveData<List<ForecastEntity>> loadFromDb() {
        return forecastDao.getAll();
    }

    private void saveResultAndReInit(final List<ForecastEntity> entities) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!entities.isEmpty()) {
                    forecastDao.deleteAll();
                }
                ForecastEntity[] arr = new ForecastEntity[entities.size()];
                entities.toArray(arr);
                forecastDao.insertAll(arr);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(), newData -> result.setValue(newData));
            }
        }.execute();
    }
}
