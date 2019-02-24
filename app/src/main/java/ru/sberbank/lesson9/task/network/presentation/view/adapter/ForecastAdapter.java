package ru.sberbank.lesson9.task.network.presentation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.presentation.view.activity.DetailForecastActivity;

import static ru.sberbank.lesson9.task.network.data.entity.ForecastEntity.FORECAST_DATE;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private static final String IMG_BASE_URL = "http://openweathermap.org/img/w/";
    private static final int IMG_SIZE = 250;

    private final LayoutInflater inflater;
    private List<ForecastItem> forecasts = Collections.emptyList();

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public String date;
        @BindView(R.id.forecast_by_day) View forecast;
        @BindView(R.id.date) TextView dateView;
        @BindView(R.id.weather) ImageView weatherView;
        @BindView(R.id.weatherDesc) TextView weatherDescView;
        @BindView(R.id.temperature) TextView temperatureView;
        ForecastViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            forecast.setOnClickListener((item) -> {
                Context context = item.getRootView().getContext();
                Intent intent = new Intent(context, DetailForecastActivity.class);
                intent.putExtra(FORECAST_DATE, date);
                context.startActivity(intent);
            });
        }
    }

    public ForecastAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        return new ForecastViewHolder(inflater.inflate(R.layout.forcast_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        ForecastItem entity = forecasts.get(position);
        holder.date = entity.getDate();
        holder.temperatureView.setText(entity.getTemp());
        holder.weatherDescView.setText(entity.getWeatherDesc());
        holder.dateView.setText(entity.getDate());
        Picasso.get().load(IMG_BASE_URL + entity.getWeather()).resize(IMG_SIZE, IMG_SIZE).into(holder.weatherView);
    }

    public void setForecasts(List<ForecastItem> forecasts) {
        this.forecasts = forecasts;
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forecasts != null ? forecasts.size() : 0;
    }
}
