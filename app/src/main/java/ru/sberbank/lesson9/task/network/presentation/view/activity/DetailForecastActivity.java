package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.databinding.ActivityDetailForecastBinding;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.DetailForecastViewModel;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.FORECAST_DATE;

public class DetailForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailForecastBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_forecast);

        DetailForecastViewModel viewModel = ViewModelProviders.of(this).get(DetailForecastViewModel.class);
        viewModel.getDetailedForecast(getIntent().getStringExtra(FORECAST_DATE));

        viewModel.getForecast().observe(this, forecast -> {
            binding.setViewmodel(forecast);
        });

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
}
