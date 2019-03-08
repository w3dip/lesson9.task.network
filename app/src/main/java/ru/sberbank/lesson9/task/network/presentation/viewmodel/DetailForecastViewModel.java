package ru.sberbank.lesson9.task.network.presentation.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.observers.DisposableSingleObserver;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastDetailsUseCase;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public class DetailForecastViewModel extends AndroidViewModel {
    private ForecastDetailsUseCase useCase;

    @Inject
    DetailForecastViewModel(Application application, ForecastDetailsUseCase useCase) {
        super(application);
        this.useCase = useCase;
    }

    public Maybe<ForecastItem> getDetailedForecast(String date) {
        useCase.setDate(date);
        return Maybe.create(emitter -> useCase.execute(new DisposableSingleObserver<ForecastItem>() {
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
        useCase.dispose();
    }
}
