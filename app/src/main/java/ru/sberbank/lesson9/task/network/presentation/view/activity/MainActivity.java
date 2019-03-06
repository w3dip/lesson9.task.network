package ru.sberbank.lesson9.task.network.presentation.view.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.presentation.view.adapter.ForecastAdapter;
import ru.sberbank.lesson9.task.network.presentation.viewmodel.ForecastViewModel;
import ru.sberbank.lesson9.task.network.utils.ViewModelFactory;

public class MainActivity extends DaggerAppCompatActivity {

    @BindView(R.id.forecasts)
    RecyclerView recyclerForecasts;

    @Inject
    ViewModelFactory viewModelFactory;

    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ForecastViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForecastViewModel.class);
        ForecastAdapter adapter = new ForecastAdapter(this);
        disposable.add(viewModel.getForecasts()
                .subscribe(forecasts -> {
                    adapter.setForecasts(forecasts);
                    recyclerForecasts.setAdapter(adapter);
                    recyclerForecasts.setLayoutManager(new LinearLayoutManager(this));
                }, Throwable::printStackTrace));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }
}
