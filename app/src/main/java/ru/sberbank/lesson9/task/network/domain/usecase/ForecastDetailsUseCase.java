package ru.sberbank.lesson9.task.network.domain.usecase;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

public class ForecastDetailsUseCase extends BaseUseCase<ForecastItem> {
    private ForecastRepository repository;
    private String date;

    @Inject
    public ForecastDetailsUseCase(ForecastRepository repository) {
        this.repository = repository;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    protected Single<ForecastItem> buildUseCaseObservable() {
        return repository.getByDate(date);
    }
}
