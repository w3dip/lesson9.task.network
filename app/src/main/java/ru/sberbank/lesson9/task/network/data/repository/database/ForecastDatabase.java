package ru.sberbank.lesson9.task.network.data.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.sberbank.lesson9.task.network.data.repository.dao.ForecastDao;
import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;

@Database(entities = {ForecastEntity.class}, version = 3)
public abstract class ForecastDatabase extends RoomDatabase {
    public abstract ForecastDao forecastDao();

    private static volatile ForecastDatabase INSTANCE;

    public static ForecastDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ForecastDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ForecastDatabase.class, "forecast_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
