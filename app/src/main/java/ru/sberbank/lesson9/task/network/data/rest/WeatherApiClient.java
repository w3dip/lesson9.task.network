package ru.sberbank.lesson9.task.network.data.rest;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;

public class WeatherApiClient {

    private static final String ROOT_URL = "http://api.openweathermap.org";
    public static final String CITY = "Saint Petersburg,ru";
    public static final String ID = "f05a6fadb8b18cace1598316cd2e6065";
    public static final String UNITS = "metric";
    public static final String LANG = "ru";

    private static Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static WeatherApi getApiClient() {
        return getRetrofitInstance().create(WeatherApi.class);
    }
}
