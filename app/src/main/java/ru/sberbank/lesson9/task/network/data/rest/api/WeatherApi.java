package ru.sberbank.lesson9.task.network.data.rest.api;

import android.arch.lifecycle.LiveData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;

public interface WeatherApi {
  @GET("/data/2.5/forecast")
  LiveData<Forecast> getWeather(@Query("q") String strCity, @Query("appid") String id, @Query("units") String units, @Query("lang") String lang);
}
