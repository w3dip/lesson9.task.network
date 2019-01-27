package ru.sberbank.lesson9.task.network.di.components;

import javax.inject.Singleton;

import dagger.Component;
import ru.sberbank.lesson9.task.network.di.modules.ForecastModule;
import ru.sberbank.lesson9.task.network.presentation.view.activity.DetailForecastActivity;
import ru.sberbank.lesson9.task.network.presentation.view.activity.MainActivity;

@Singleton
@Component(modules = ForecastModule.class)
public interface ForecastComponent {
  void inject(MainActivity activity);
  void inject(DetailForecastActivity activity);
}
