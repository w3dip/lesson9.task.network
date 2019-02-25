package ru.sberbank.lesson9.task.network.presentation.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.observers.DisposableSingleObserver;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public class DetailForecastViewModel extends AndroidViewModel {
    private ForecastDetailsInteractor interactor;

    @Inject
    public DetailForecastViewModel(Application application, ForecastDetailsInteractor interactor) {
        super(application);
        this.interactor = interactor;
    }

    public Maybe<ForecastItem> getDetailedForecast(String date) {
        interactor.setDate(date);
        return Maybe.create(emitter -> interactor.execute(new DisposableSingleObserver<ForecastItem>() {
                @Override
                public void onSuccess(ForecastItem forecastItem) {
                    emitter.onSuccess(forecastItem);
                }

                @Override
                public void onError(Throwable e) {
                    emitter.onError(e);
                }
            }));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        interactor.dispose();
    }
}
