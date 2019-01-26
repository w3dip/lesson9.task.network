package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import java.util.List;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

public class ForecastItemToEntityMapper implements Mapper<List<ForecastItem>, List<ForecastEntity>> {
    @Override
    public List<ForecastEntity> map(List<ForecastItem> source) {
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
