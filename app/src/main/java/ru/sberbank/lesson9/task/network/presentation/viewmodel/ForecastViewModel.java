package ru.sberbank.lesson9.task.network.presentation.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.observers.DisposableSingleObserver;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastGetListUseCase;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastPersistUseCase;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class ForecastViewModel extends AndroidViewModel {
    private ForecastGetListUseCase getListInteractor;
    private ForecastPersistUseCase persistInteractor;

    @Inject
    ForecastViewModel(Application application, ForecastGetListUseCase getListInteractor, ForecastPersistUseCase persistInteractor) {
        super(application);
        this.getListInteractor = getListInteractor;
        this.persistInteractor = persistInteractor;
        this.getListInteractor.setNetworkAvailable(checkConnection(application.getApplicationContext()));
    }

    public Maybe<List<ForecastItem>> getForecasts() {
        return Maybe.create(emitter -> getListInteractor.execute(new DisposableSingleObserver<List<ForecastItem>>() {
            @Override
            public void onSuccess(List<ForecastItem> forecastItems) {
                persistInteractor.setForecastItems(forecastItems);
                persistInteractor.execute(new DisposableSingleObserver<List<Long>>() {
                    @Override
                    public void onSuccess(List<Long> longs) {
                        emitter.onSuccess(forecastItems);
                    }
                    @Override
                    public void onError(Throwable e) {
                        emitter.onError(e);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                emitter.onError(e);
            }
        }));

    }
}
