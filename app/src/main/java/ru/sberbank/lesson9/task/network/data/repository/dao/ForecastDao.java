package ru.sberbank.lesson9.task.network.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.COLUMN_DATE;
import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.TABLE_NAME;

@Dao
public interface ForecastDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    Single<List<ForecastEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(ForecastEntity... forecasts);

    @Query("DELETE FROM " + TABLE_NAME)
    void deleteAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = :date")
    Single<ForecastEntity> getByDate(String date);
}
