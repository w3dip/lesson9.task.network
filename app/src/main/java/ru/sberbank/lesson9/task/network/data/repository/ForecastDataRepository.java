package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToEntityMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastEntityToItemMapper;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.CITY;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.ID;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.LANG;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.UNITS;

@Singleton
public class ForecastDataRepository implements ForecastRepository {
    private static Mapper<Forecast, List<ForecastEntity>> forecastInfoToEntityMapper = new ForcastInfoToEntityMapper();
    private static Mapper<List<ForecastEntity>, List<ForecastItem>> forecastEntityToItemMapper = new ForecastEntityToItemMapper();

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    private final MediatorLiveData<List<ForecastEntity>> result = new MediatorLiveData<>();
    private final MediatorLiveData<ForecastEntity> resultByDate = new MediatorLiveData<>();

    @Inject
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
            result.addSource(apiResponse, this::saveResultAndReInit);
        } else {
            result.addSource(loadFromDb(), result::setValue);
        }
        return Transformations.map(result, input -> forecastEntityToItemMapper.map(input));
    }

    @Override
    public LiveData<ForecastItem> getByDate(String date) {
        new AsyncTask<String, Void, LiveData<ForecastEntity>>() {
            @Override
            protected LiveData<ForecastEntity> doInBackground(String... ids) {
                return forecastDao.getByDate(ids[0]);
            }

            @Override
            protected void onPostExecute(LiveData<ForecastEntity> entity) {
                resultByDate.addSource(entity, resultByDate::setValue);
            }
        }.execute(date);
        return Transformations.map(resultByDate, input -> forecastEntityToItemMapper.map(Lists.newArrayList(input)).get(0));
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
                result.addSource(loadFromDb(), result::setValue);
            }
        }.execute();
    }
}
