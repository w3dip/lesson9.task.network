package ru.sberbank.lesson9.task.network.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;

import static ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity.COLUMN_DATE;
import static ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity.TABLE_NAME;

@Dao
public interface ForecastDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    LiveData<List<ForecastEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(ForecastEntity... forecasts);

    @Query("DELETE FROM " + TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = :date")
    ForecastEntity getByDate(String date);
}
