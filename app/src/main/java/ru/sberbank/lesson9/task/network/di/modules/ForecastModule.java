package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.domain.interactor.UseCase;
import ru.sberbank.lesson9.task.network.domain.interactor.usecase.ForecastDetailsInteractor;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

@Module
public abstract class ForecastModule {
    @Binds
    public abstract ForecastRepository bindForecastRepository(ForecastDataRepository repository);

    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract UseCase<ForecastItem> bindForecastDetailsInteractor(ForecastDetailsInteractor interactor);
}
