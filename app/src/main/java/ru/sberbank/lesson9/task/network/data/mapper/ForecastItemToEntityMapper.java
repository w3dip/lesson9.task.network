package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public class ForecastItemToEntityMapper implements Mapper<List<ForecastItem>, List<ForecastEntity>> {
    @Override
    public List<ForecastEntity> map(List<ForecastItem> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return FluentIterable.from(source)
                .transform(item -> ForecastEntity.builder()
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
