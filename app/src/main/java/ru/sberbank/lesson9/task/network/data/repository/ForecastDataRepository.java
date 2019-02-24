package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;

import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.data.mapper.ForcastInfoToEntityMapper;
import ru.sberbank.lesson9.task.network.data.mapper.ForecastEntityToItemMapper;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

import static ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi.*;

@Singleton
public class ForecastDataRepository implements ForecastRepository {
    private static Mapper<Forecast, List<ForecastEntity>> forecastInfoToEntityMapper = new ForcastInfoToEntityMapper();
    private static Mapper<List<ForecastEntity>, List<ForecastItem>> forecastEntityToItemMapper = new ForecastEntityToItemMapper();

    private WeatherApi weatherApi;
    private ForecastDao forecastDao;

    private final MediatorLiveData<List<ForecastEntity>> result = new MediatorLiveData<>();
    private final MediatorLiveData<ForecastEntity> resultByDate = new MediatorLiveData<>();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public ForecastDataRepository(WeatherApi weatherApi, ForecastDao forecastDao) {
        this.weatherApi = weatherApi;
        this.forecastDao = forecastDao;
    }

    @Override
    public LiveData<List<ForecastItem>> getAll(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            loadWeather();
        } else {
            loadFromDb();
        }
        return Transformations.map(result, input -> forecastEntityToItemMapper.map(input));
    }

    @Override
    public LiveData<ForecastItem> getByDate(String date) {
        disposable.add(forecastDao.getByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ForecastEntity>() {
                    @Override
                    public void onSuccess(ForecastEntity entity) {
                        resultByDate.setValue(entity);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
        return Transformations.map(resultByDate, input -> forecastEntityToItemMapper.map(Lists.newArrayList(input)).get(0));
    }

    private void loadFromDb() {
        disposable.add(forecastDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ForecastEntity>>() {
                    @Override
                    public void onSuccess(List<ForecastEntity> entities) {
                        result.setValue(entities);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }

    private void loadWeather() {
        disposable.add(weatherApi.getWeather(CITY, ID, UNITS, LANG)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Forecast>() {
                    @Override
                    public void onSuccess(Forecast forecast) {
                        List<ForecastEntity> entities = forecastInfoToEntityMapper.map(forecast);
                        result.setValue(entities);
                        saveResultAndReInit(entities);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }));
    }

    private void saveResultAndReInit(final List<ForecastEntity> entities) {
        disposable.add(
            Single.create((SingleOnSubscribe<Void>) emitter -> {
                if (!entities.isEmpty()) {
                    forecastDao.deleteAll();
                }
                ForecastEntity[] arr = new ForecastEntity[entities.size()];
                entities.toArray(arr);
                forecastDao.insertAll(arr);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    loadFromDb();
                }

                @Override
                public void onError(Throwable e) {
                }
            }));
    }
}
