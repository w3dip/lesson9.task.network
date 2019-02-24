package ru.sberbank.lesson9.task.network.di.modules;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import ru.sberbank.lesson9.task.network.data.repository.ForecastDataRepository;
import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.repository.database.ForecastDatabase;
import ru.sberbank.lesson9.task.network.domain.repository.ForecastRepository;
//import ru.sberbank.lesson9.task.network.presentation.view.viewmodel.ForecastViewModel;

@Module
public abstract class ForecastModule {
    @Binds
    public abstract ForecastRepository bindForecastRepository(ForecastDataRepository repository);

  /*private final Application application;

  public ForecastModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return application.getApplicationContext();
  }

  @Provides
  @Singleton
  ForecastDao provideForecastDao() {
    return ForecastDatabase.getDatabase(application).forecastDao();
  }

  @Provides
  @Singleton
  ForecastViewModel provideForecastViewModel() {
    return ViewModelProviders.of(application.getApplicationContext()).get(ForecastViewModel.class);
  }*/
}
