package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.data.entity.ForecastEntity;
import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.model.generated.Main;
import ru.sberbank.lesson9.task.network.domain.model.generated.Weather;

public class ForcastInfoToEntityMapper implements Mapper<Forecast, List<ForecastEntity>> {
    private static final String DATE_FORMAT_INPUT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_OUTPUT = "dd.MM.yyyy HH:mm";
    private static final String DEGREE  = "\u00b0";

    @Override
    public List<ForecastEntity> map(Forecast source) {
        if (source == null || source.getInfo() == null || source.getInfo().isEmpty()) {
            return Collections.emptyList();
        }
        return FluentIterable.from(source.getInfo())
                .transform(input -> {
                    Main main = input.getMain();
                    Weather weather = input.getWeather().get(0);
                    DateTimeFormatter inputFormatter = DateTimeFormat.forPattern(DATE_FORMAT_INPUT);
                    DateTimeFormatter outputFormatter = DateTimeFormat.forPattern(DATE_FORMAT_OUTPUT);
                    DateTime dateTime = inputFormatter.parseDateTime(input.getDtTxt());
                    long temp = Math.round(main.getTemp());
                    String tempStr = String.valueOf(temp) + DEGREE;
                    return ForecastEntity.builder()
                            .date(outputFormatter.print(dateTime))
                            .weather(weather.getIcon() + ".png")
                            .weatherDesc(weather.getDescription())
                            .temp(temp > 0 ? "+" + tempStr : tempStr)
                            .wind(String.valueOf(Math.round(input.getWind().getSpeed())) + " м/c")
                            .humidity(String.valueOf(main.getHumidity()) + "%")
                            .pressure(String.valueOf(Math.round(main.getGrndLevel())) + " гПа")
                            .build();
                })
                .toList();
    }
}
