package ru.sberbank.lesson9.task.network.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.R;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private final LayoutInflater inflater;
    private List<ForecastItem> forecasts = Collections.emptyList();

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public TextView temperatureView;
        public ForecastViewHolder(View v) {
            super(v);
            temperatureView = v.findViewById(R.id.temperature);
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
        ForecastItem item = forecasts.get(position);
        long temp = Math.round(item.getTemp());
        String tempStr = String.valueOf(temp);
        holder.temperatureView.setText(temp > 0 ? "+" + tempStr : tempStr);
    }

    public void setForecasts(List<ForecastItem> forecasts) {
        this.forecasts = forecasts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }
}
