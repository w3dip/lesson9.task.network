package ru.sberbank.lesson9.task.network.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastGetListUseCase extends BaseUseCase<List<ForecastItem>> {
    private ForecastRepository repository;
    private boolean isNetworkAvailable;

    @Inject
    public ForecastGetListUseCase(ForecastRepository repository) {
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
