/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.sberbank.lesson9.task.network.data.rest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sberbank.lesson9.task.network.domain.model.Forecast;

public interface WeatherApi {
  @GET("/data/2.5/forecast")
  Call<Forecast> getWeather(@Query("q") String strCity, @Query("appid") String id, @Query("units") String units, @Query("lang") String lang);
}
