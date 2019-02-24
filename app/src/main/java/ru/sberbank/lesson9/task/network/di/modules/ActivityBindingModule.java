package ru.sberbank.lesson9.task.network.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.sberbank.lesson9.task.network.presentation.view.activity.DetailForecastActivity;
import ru.sberbank.lesson9.task.network.presentation.view.activity.MainActivity;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract DetailForecastActivity bindDetailForecastActivity();
}
