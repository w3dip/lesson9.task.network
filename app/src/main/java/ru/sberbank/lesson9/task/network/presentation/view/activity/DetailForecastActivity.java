package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ru.sberbank.lesson9.task.network.NetworkApplication;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.presentation.view.BaseView;
import ru.sberbank.lesson9.task.network.presentation.view.viewmodel.DetailForecastViewModel;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.FORECAST_DATE;

public class DetailForecastActivity extends AppCompatActivity implements BaseView {

    private DetailForecastViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_forecast);

        ((NetworkApplication)getApplication()).getForecastComponent().inject(this);

        viewModel = ViewModelProviders.of(this).get(DetailForecastViewModel.class);
        viewModel.getDetailedForecast(getIntent().getStringExtra(FORECAST_DATE), this);

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

    @Override
    public void handle(ForecastItem forecast) {
        findAndSetValue(R.id.detailTemperature, forecast.getTemp());
        findAndSetValue(R.id.detailWeather, forecast.getWeatherDesc());
        findAndSetValue(R.id.detailWind, forecast.getWind());
        findAndSetValue(R.id.detailHumidity, forecast.getHumidity());
        findAndSetValue(R.id.detailPressure, forecast.getPressure());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void findAndSetValue(int id, String value) {
        ((TextView)findViewById(id)).setText(value);
    }
}
