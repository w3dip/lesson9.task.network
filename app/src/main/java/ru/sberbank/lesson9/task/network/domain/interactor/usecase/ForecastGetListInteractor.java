package ru.sberbank.lesson9.task.network.domain.interactor.usecase;

import java.util.List;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.interactor.UseCase;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastGetListInteractor extends UseCase<List<ForecastItem>> {
    private ForecastRepository repository;
    private boolean isNetworkAvailable;

    public ForecastGetListInteractor(ForecastRepository repository) {
        this.repository = repository;
    }

    public void setNetworkAvailable(boolean networkAvailable) {
        isNetworkAvailable = networkAvailable;
    }

    @Override
    protected Single<List<ForecastItem>> buildUseCaseObservable() {
        return repository.getAll(isNetworkAvailable);
    }
}
