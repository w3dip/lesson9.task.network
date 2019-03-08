package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;
import android.content.Context;

import java.util.List;

import dagger.Binds;
import dagger.Module;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.domain.usecase.BaseUseCase;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastDetailsUseCase;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastGetListUseCase;
import ru.sberbank.lesson9.task.network.domain.usecase.ForecastPersistUseCase;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

@Module
public abstract class ForecastModule {
    @Binds
    public abstract ForecastRepository bindForecastRepository(ForecastDataRepository repository);

    @Binds
    abstract Context bindContext(Application application);

    @Binds
    abstract BaseUseCase<ForecastItem> bindForecastDetailsUseCase(ForecastDetailsUseCase useCase);

    @Binds
    abstract BaseUseCase<List<ForecastItem>> bindForecastGetListUseCase(ForecastGetListUseCase useCase);

    @Binds
    abstract BaseUseCase<List<Long>> bindForecastPersistUseCase(ForecastPersistUseCase useCase);
}
