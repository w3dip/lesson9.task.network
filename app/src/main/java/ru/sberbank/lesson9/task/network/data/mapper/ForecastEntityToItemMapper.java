package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public class ForecastEntityToItemMapper implements Mapper<List<ForecastEntity>, List<ForecastItem>> {
    @Override
    public List<ForecastItem> map(List<ForecastEntity> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return FluentIterable.from(source)
                .transform(item -> ForecastItem.builder()
                        .date(item.getDate())
                        .temp(item.getTemp())
                        .weather(item.getWeather())
                        .weatherDesc(item.getWeatherDesc())
                        .wind(item.getWind())
                        .humidity(item.getHumidity())
                        .pressure(item.getPressure())
                        .build())
                .toList();

    }
}
