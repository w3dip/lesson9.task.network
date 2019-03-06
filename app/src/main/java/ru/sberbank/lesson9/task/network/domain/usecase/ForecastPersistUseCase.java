package ru.sberbank.lesson9.task.network.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastPersistUseCase extends BaseUseCase<List<Long>> {
    private ForecastRepository repository;
    private List<ForecastItem> forecastItems;

    @Inject
    public ForecastPersistUseCase(ForecastRepository repository) {
        this.repository = repository;
    }

    public void setForecastItems(List<ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @Override
    protected Single<List<Long>> buildUseCaseObservable() {
        return repository.persist(forecastItems);
    }
}
