package ru.sberbank.lesson9.task.network.di.modules;

import dagger.Binds;
import dagger.Module;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;

@Module
public abstract class ForecastModule {
    @Binds
    public abstract ForecastRepository bindForecastRepository(ForecastDataRepository repository);
}
