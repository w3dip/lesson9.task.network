package ru.sberbank.lesson9.task.network;

import android.app.Application;

import com.facebook.stetho.Stetho;

import ru.sberbank.lesson9.task.network.di.components.DaggerForecastComponent;
import ru.sberbank.lesson9.task.network.di.components.ForecastComponent;
import ru.sberbank.lesson9.task.network.di.modules.ForecastModule;

public class NetworkApplication extends Application {
    private ForecastComponent forecastComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        Stetho.initializeWithDefaults(this);
    }

    private void initializeInjector() {
        forecastComponent = DaggerForecastComponent
                .builder()
                .forecastModule(new ForecastModule(this))
                .build();
    }

    public ForecastComponent getForecastComponent() {
        return forecastComponent;
    }
}
