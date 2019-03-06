package ru.sberbank.lesson9.task.network.domain.interactor.usecase;

import java.util.List;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.interactor.UseCase;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastPersistInteractor extends UseCase<List<Long>> {
    private ForecastRepository repository;
    private List<ForecastItem> forecastItems;

    public ForecastPersistInteractor(ForecastRepository repository) {
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
