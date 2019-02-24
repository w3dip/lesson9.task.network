package ru.sberbank.lesson9.task.network.di.components;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;
import ru.sberbank.lesson9.task.network.NetworkApplication;
import ru.sberbank.lesson9.task.network.di.modules.ActivityBindingModule;
import ru.sberbank.lesson9.task.network.di.modules.ApplicationModule;
import ru.sberbank.lesson9.task.network.di.modules.ContextModule;
import ru.sberbank.lesson9.task.network.di.modules.ForecastModule;

@Singleton
@Component(modules = {ContextModule.class, ApplicationModule.class, AndroidSupportInjectionModule.class, ActivityBindingModule.class})
public interface ForecastComponent extends AndroidInjector<DaggerApplication> {
  void inject(NetworkApplication application);

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);
    ForecastComponent build();
  }
}
