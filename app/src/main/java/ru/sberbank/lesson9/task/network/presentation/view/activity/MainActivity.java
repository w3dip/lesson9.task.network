package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.common.collect.FluentIterable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient;
import ru.sberbank.lesson9.task.network.data.rest.api.WeatherApi;
import ru.sberbank.lesson9.task.network.domain.model.Forecast;
import ru.sberbank.lesson9.task.network.domain.model.Info;
import ru.sberbank.lesson9.task.network.presentation.mapper.ForcastInfoToItemMapper;
import ru.sberbank.lesson9.task.network.presentation.mapper.Mapper;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;
import ru.sberbank.lesson9.task.network.presentation.view.adapter.ForecastAdapter;

import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.CITY;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.ID;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.LANG;
import static ru.sberbank.lesson9.task.network.data.rest.WeatherApiClient.UNITS;
import static ru.sberbank.lesson9.task.network.utils.InternetConnection.checkConnection;

public class MainActivity extends AppCompatActivity {

    private static Mapper<Info, ForecastItem> forecastItemMapper = new ForcastInfoToItemMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        loadForecasts();
        super.onStart();
    }

    protected void loadForecasts() {
        if (checkConnection(getApplicationContext())) {
            final ProgressDialog dialog;

            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Getting JSON data");
            dialog.setMessage("Please wait...");
            dialog.show();

            WeatherApi api = WeatherApiClient.getApiClient();
            Call<Forecast> call = api.getWeather(CITY, ID, UNITS, LANG);

            call.enqueue(new Callback<Forecast>() {
                @Override
                public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                    dialog.dismiss();

                    if(response.isSuccessful()) {

                        List<ForecastItem> forecasts = FluentIterable.from(response.body().getInfo())
                                .transform(info -> forecastItemMapper.map(info))
                                .toList();

                        RecyclerView recyclerForecasts = findViewById(R.id.forecasts);
                        ForecastAdapter adapter = new ForecastAdapter(MainActivity.this);
                        adapter.setForecasts(forecasts);
                        recyclerForecasts.setAdapter(adapter);
                        recyclerForecasts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    } else {
                        Log.e(this.getClass().getName(), "Response is not successfull");
                       // Snackbar.make(findViewById(R.id.layoutMain), "Something going wrong!", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Forecast> call, Throwable t) {
                    dialog.dismiss();
                    t.printStackTrace();
                }
            });

        } else {
            Log.e(this.getClass().getName(), "Network is not accessable");
            //Snackbar.make(findViewById(R.id.layoutMain), "Check your internet connection!", Snackbar.LENGTH_LONG).show();
        }
    }
}
