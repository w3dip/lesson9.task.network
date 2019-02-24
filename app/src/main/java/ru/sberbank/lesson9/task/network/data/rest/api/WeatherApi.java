package ru.sberbank.lesson9.task.network.data.rest.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;

public interface WeatherApi {
  public static final String CITY = "Saint Petersburg,ru";
  public static final String ID = "f05a6fadb8b18cace1598316cd2e6065";
  public static final String UNITS = "metric";
  public static final String LANG = "ru";

  @GET("/data/2.5/forecast")
  Single<Forecast> getWeather(@Query("q") String strCity, @Query("appid") String id, @Query("units") String units, @Query("lang") String lang);
}
