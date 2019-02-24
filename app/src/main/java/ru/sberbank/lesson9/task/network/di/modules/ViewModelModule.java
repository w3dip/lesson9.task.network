package ru.sberbank.lesson9.task.network.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.sberbank.lesson9.task.network.di.util.ViewModelKey;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.DetailForecastViewModel;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.ForecastViewModel;
import ru.sberbank.lesson9.task.network.utils.ViewModelFactory;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel.class)
    abstract ViewModel bindForecastViewModel(ForecastViewModel forecastViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailForecastViewModel.class)
    abstract ViewModel bindDetailForecastViewModel(DetailForecastViewModel detailForecastViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
