package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.Forecast;
import ru.sberbank.lesson9.task.network.domain.model.Main;
import ru.sberbank.lesson9.task.network.domain.model.Weather;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

public class ForcastInfoToItemMapper implements Mapper<Forecast, List<ForecastItem>> {
    private static final String DATE_FORMAT_INPUT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_OUTPUT = "dd.MM.yyyy HH:mm";
    private static final String DEGREE  = "\u00b0";

    @Override
    public List<ForecastItem> map(Forecast source) {
        if (source == null) {
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
                    return ForecastItem.builder()
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
