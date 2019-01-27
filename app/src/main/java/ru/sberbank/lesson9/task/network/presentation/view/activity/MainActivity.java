package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import javax.inject.Inject;

import ru.sberbank.lesson9.task.network.NetworkApplication;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.presentation.view.adapter.ForecastAdapter;
import ru.sberbank.lesson9.task.network.presentation.view.viewmodel.ForecastViewModel;

public class MainActivity extends AppCompatActivity {
    @Inject
    protected ForecastViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NetworkApplication)getApplication()).getForecastComponent().inject(this);

        viewModel.getForecasts().observe(this, forecasts -> {
            RecyclerView recyclerForecasts = findViewById(R.id.forecasts);
            ForecastAdapter adapter = new ForecastAdapter(this);
            adapter.setForecasts(forecasts);
            recyclerForecasts.setAdapter(adapter);
            recyclerForecasts.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}
