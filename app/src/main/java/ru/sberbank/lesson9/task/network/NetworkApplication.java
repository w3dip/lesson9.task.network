package ru.sberbank.lesson9.task.network;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class NetworkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
