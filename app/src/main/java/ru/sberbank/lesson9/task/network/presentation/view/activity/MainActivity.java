package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.presentation.view.adapter.ForecastAdapter;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.ForecastViewModel;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.forecasts)
    RecyclerView recyclerForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ForecastViewModel viewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        viewModel.getForecasts().observe(this, forecasts -> {
            ForecastAdapter adapter = new ForecastAdapter(this);
            adapter.setForecasts(forecasts);
            recyclerForecasts.setAdapter(adapter);
            recyclerForecasts.setLayoutManager(new LinearLayoutManager(this));
        });
    }
}
