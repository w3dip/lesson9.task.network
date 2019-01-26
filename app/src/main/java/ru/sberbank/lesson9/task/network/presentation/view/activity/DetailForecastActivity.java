package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.presentation.view.viewmodel.DetailForecastViewModel;

import static ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity.FORECAST_DATE;

public class DetailForecastActivity extends AppCompatActivity implements BaseView {

    private DetailForecastViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_forecast);

        Intent intent = getIntent();
        String currentForecastDate = intent.getStringExtra(FORECAST_DATE);

        viewModel = ViewModelProviders.of(this).get(DetailForecastViewModel.class);
        viewModel.getDetailedForecast(currentForecastDate, this);

        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void handle(ForecastEntity forecast) {
        TextView detailTemperature = findViewById(R.id.detailTemperature);
        detailTemperature.setText(forecast.getTemp());
        TextView detailWeather = findViewById(R.id.detailWeather);
        detailWeather.setText(forecast.getWeatherDesc());
        TextView detailWind = findViewById(R.id.detailWind);
        detailWind.setText(forecast.getWind());
        TextView detailHumidity = findViewById(R.id.detailHumidity);
        detailHumidity.setText(forecast.getHumidity());
        TextView detailPressure = findViewById(R.id.detailPressure);
        detailPressure.setText(forecast.getPressure());
    }
}
