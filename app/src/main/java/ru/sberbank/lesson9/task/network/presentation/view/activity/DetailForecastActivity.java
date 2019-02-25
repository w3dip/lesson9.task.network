package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.databinding.ActivityDetailForecastBinding;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.DetailForecastViewModel;
import ru.sberbank.lesson9.task.network.utils.ViewModelFactory;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.FORECAST_DATE;

public class DetailForecastActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailForecastViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailForecastViewModel.class);
        disposable.add(viewModel.getDetailedForecast(getIntent().getStringExtra(FORECAST_DATE))
                .subscribe(forecastItem -> {
                    ActivityDetailForecastBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_forecast);
                    binding.setViewmodel(forecastItem);
                }, Throwable::printStackTrace));

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
    protected void onDestroy() {
        super.onDestroy();
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
