package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.List;

import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.presentation.view.adapter.ForecastAdapter;
import ru.sberbank.lesson9.task.network.presentation.view.viewmodel.ForecastViewModel;

public class MainActivity extends AppCompatActivity {
    private ForecastViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        viewModel.getForecasts().observe(this, new Observer<List<ForecastEntity>>() {
            @Override
            public void onChanged(@Nullable List<ForecastEntity> forecasts) {
                RecyclerView recyclerForecasts = findViewById(R.id.forecasts);
                ForecastAdapter adapter = new ForecastAdapter(MainActivity.this);
                adapter.setForecasts(forecasts);
                recyclerForecasts.setAdapter(adapter);
                recyclerForecasts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
