package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.google.common.collect.Lists;

import java.util.List;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToEntityMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastEntityToItemMapper;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.interactor.Callback;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.CITY;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.ID;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.LANG;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.UNITS;

public class ForecastDataRepository implements ForecastRepository {
    private static Mapper<Forecast, List<ForecastEntity>> forecastInfoToEntityMapper = new ForcastInfoToEntityMapper();
    private static Mapper<List<ForecastEntity>, List<ForecastItem>> forecastEntityToItemMapper = new ForecastEntityToItemMapper();

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
    public LiveData<List<ForecastItem>> getAll(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            final LiveData<List<ForecastEntity>> apiResponse = Transformations
                    .map(weatherApi.getWeather(CITY, ID, UNITS, LANG),
                            forecast -> forecastInfoToEntityMapper.map(forecast)
                    );
            result.addSource(apiResponse, response -> {
                saveResultAndReInit(response);
            });
        } else {
            result.addSource(loadFromDb(), newData -> result.setValue(newData));
        }
        return Transformations.map(result, input -> forecastEntityToItemMapper.map(input));
    }

    @Override
    public void getByDate(String date, Callback<ForecastItem> callback) {
        new AsyncTask<String, Void, ForecastEntity>() {
            @Override
            protected ForecastEntity doInBackground(String... ids) {
                return forecastDao.getByDate(ids[0]);
            }

            @Override
            protected void onPostExecute(ForecastEntity forecastEntity) {
                callback.handle(forecastEntityToItemMapper.map(Lists.newArrayList(forecastEntity)).get(0));
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
