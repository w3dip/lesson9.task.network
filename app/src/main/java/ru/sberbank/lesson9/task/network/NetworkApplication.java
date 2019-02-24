package ru.sberbank.lesson9.task.network;

import android.app.Application;

import com.facebook.stetho.Stetho;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import ru.sberbank.lesson9.task.network.di.components.DaggerForecastComponent;
import ru.sberbank.lesson9.task.network.di.components.ForecastComponent;
import ru.sberbank.lesson9.task.network.di.modules.ForecastModule;

public class NetworkApplication extends DaggerApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ForecastComponent component = DaggerForecastComponent.builder().application(this).build();
        component.inject(this);
        return component;
    }
}
