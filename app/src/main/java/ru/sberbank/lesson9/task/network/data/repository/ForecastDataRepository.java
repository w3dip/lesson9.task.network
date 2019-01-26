package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.List;
import java.util.concurrent.Executor;

import ru.sberbank.lesson9.task.network.data.mapper.ForecastItemToEntityMapper;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.data.utils.ForecastsDownloader;
import ru.sberbank.lesson9.task.network.data.utils.Function;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToItemMapper;
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
        LiveData<List<ForecastEntity>> liveData = new ForecastsDownloader<List<ForecastEntity>, List<Forecast>, List<ForecastItem>>(forecastEntityMapper, isNetworkAvailable) {
            @Override
            protected void saveCallResult(@NonNull List<ForecastEntity> items) {
                //items = filterForecasts(items, DateTime.now());
                if (!items.isEmpty()) {
                    forecastDao.deleteAll();
                }
                ForecastEntity[] arr = new ForecastEntity[items.size()];
                items.toArray(arr);
                forecastDao.insertAll(arr);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ForecastEntity> data) {
                //return filterForecasts(data, DateTime.now()).isEmpty();//let's always refresh to be up to date. data == null || data.isEmpty();
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ForecastEntity>> loadFromDb() {
                /*LiveData<List<ForecastEntity>> forecasts = forecastDao.getAll();
                LiveData<List<ForecastEntity>> result = Transformations.map(forecasts, forecast -> {
                    return FluentIterable.from(forecast)
                            .filter(new Predicate<ForecastEntity>() {
                                @Override
                                public boolean apply(ForecastEntity input) {
                                    return true;
                                }
                            })
                            .toList();
                });*/
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
                //super.onPostExecute(forecastEntity);
                handler.apply(forecastEntity);
            }
        }.execute(date);
    }

    /*@Override
    public void save(ForecastEntity forecast) {

    }*/

    /*public void clear() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                forecastDao.deleteAll();
            }
        });
    }*/

    /*private List<ForecastEntity> filterForecasts(List<ForecastEntity> source, DateTime current) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT_OUTPUT);
        //final DateTime startDate = current.minusHours(3);
        final DateTime endDate = current.plusDays(5).minusHours(3);
        return FluentIterable.from(source)
                .filter(new Predicate<ForecastEntity>() {
                    @Override
                    public boolean apply(ForecastEntity input) {
                        DateTime dateTime = formatter.parseDateTime(input.getDate());
                        //return dateTime.isBefore(startDate) || dateTime.isAfter(endDate);
                        return dateTime.isAfter(endDate);
                    }
                }).toList();

    }*/
}
