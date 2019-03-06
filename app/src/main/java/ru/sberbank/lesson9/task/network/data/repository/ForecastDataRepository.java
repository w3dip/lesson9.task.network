package ru.sberbank.lesson9.task.network.data.repository;

import com.google.common.collect.FluentIterable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToItemMapper;
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
    private static Mapper<Forecast, List<ForecastItem>> forecastInfoToItemMapper = new ForcastInfoToItemMapper();
    private static Mapper<ForecastEntity, ForecastItem> forecastEntityToItemMapper = new ForecastEntityToItemMapper();
    private static Mapper<ForecastItem, ForecastEntity> forecastItemToEntityMapper = new ForecastItemToEntityMapper();

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    @Inject
    ForecastDataRepository(WeatherApi weatherApi, ForecastDao forecastDao) {
        this.weatherApi = weatherApi;
        this.forecastDao = forecastDao;
    }

    @Override
    public Single<List<ForecastItem>> getAll(boolean isNetworkAvailable) {
        return isNetworkAvailable ? loadWeather() : loadFromDb();
    }

    @Override
    public Single<ForecastItem> getByDate(String date) {
        return forecastDao.getByDate(date)
                .map(forecastEntityToItemMapper::map);
    }

    @Override
    public Single<List<Long>> persist(List<ForecastItem> items) {
        return Single.fromCallable(() -> {
            if (!items.isEmpty()) {
                forecastDao.deleteAll();
            }
            return forecastDao.insertAll(
                    toArray(FluentIterable.from(items)
                            .transform(forecastItemToEntityMapper::map)
                            .toList(), ForecastEntity.class)
            );
        });
    }

    private Single<List<ForecastItem>> loadFromDb() {
        return forecastDao.getAll()
                .flatMap(entities -> Observable.fromIterable(entities)
                        .map(forecastEntityToItemMapper::map)
                        .toList());
    }

    private Single<List<ForecastItem>> loadWeather() {
        return weatherApi.getWeather(CITY, ID, UNITS, LANG)
                .map(forecastInfoToItemMapper::map);
    }
}
