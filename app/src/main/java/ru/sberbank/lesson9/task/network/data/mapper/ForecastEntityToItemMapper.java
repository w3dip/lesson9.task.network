package ru.sberbank.lesson9.task.network.data.mapper;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public class ForecastEntityToItemMapper implements Mapper<ForecastEntity, ForecastItem> {
    @Override
    public ForecastItem map(ForecastEntity from) {
        return ForecastItem.builder()
                .date(from.getDate())
                .temp(from.getTemp())
                .weather(from.getWeather())
                .weatherDesc(from.getWeatherDesc())
                .wind(from.getWind())
                .humidity(from.getHumidity())
                .pressure(from.getPressure())
                .build();
    }
}
