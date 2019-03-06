package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.MediatorLiveData;

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToEntityMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastEntityToItemMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastItemToEntityMapper;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static com.google.common.collect.Iterables.toArray;
import static ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi.CITY;
import static ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi.ID;
import static ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi.LANG;
import static ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi.UNITS;

@Singleton
public class ForecastDataRepository implements ForecastRepository {
    private static Mapper<Forecast, List<ForecastEntity>> forecastInfoToEntityMapper = new ForcastInfoToEntityMapper();
    private static Mapper<List<ForecastEntity>, List<ForecastItem>> forecastEntityToItemMapper = new ForecastEntityToItemMapper();
    private static Mapper<List<ForecastItem>, List<ForecastEntity>> forecastItemToEntityMapper = new ForecastItemToEntityMapper();

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    private final MediatorLiveData<List<ForecastEntity>> result = new MediatorLiveData<>();

    @Inject
    public ForecastDataRepository(WeatherApi weatherApi, ForecastDao forecastDao) {
        this.weatherApi = weatherApi;
        this.forecastDao = forecastDao;
    }

    @Override
    public Single<List<ForecastItem>> getAll(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return loadWeather();
        } else {
            return loadFromDb();
        }
    }

    @Override
    public Single<ForecastItem> getByDate(String date) {
        //TODO сделать маппер для одного значения
        return forecastDao.getByDate(date)
                .map(entity -> forecastEntityToItemMapper.map(Lists.newArrayList(entity)).get(0));
    }

    @Override
    public Single<List<Long>> persist(List<ForecastItem> items) {
        return Single.fromCallable(() -> {
            if (!items.isEmpty()) {
                forecastDao.deleteAll();
            }
            return forecastDao.insertAll(toArray(forecastItemToEntityMapper.map(items), ForecastEntity.class));
        });
    }

    private Single<List<ForecastItem>> loadFromDb() {
        return forecastDao.getAll()
                .map(forecastEntityToItemMapper::map);
    }

    private Single<List<ForecastItem>> loadWeather() {
        //TODO сделать один маппер
        return weatherApi.getWeather(CITY, ID, UNITS, LANG)
                .map(forecastInfoToEntityMapper::map)
                .map(forecastEntityToItemMapper::map);
    }
}
