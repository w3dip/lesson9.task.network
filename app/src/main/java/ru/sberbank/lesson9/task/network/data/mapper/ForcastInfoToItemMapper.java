package ru.sberbank.lesson9.task.network.data.mapper;

import com.google.common.collect.FluentIterable;

import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

import ru.sberbank.lesson9.task.network.domain.mapper.Mapper;
import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;
import ru.sberbank.lesson9.task.network.domain.model.generated.Forecast;
import ru.sberbank.lesson9.task.network.domain.model.generated.Main;
import ru.sberbank.lesson9.task.network.domain.model.generated.Weather;

import static java.lang.Math.round;
import static java.lang.String.valueOf;
import static org.joda.time.format.DateTimeFormat.forPattern;

public class ForcastInfoToItemMapper implements Mapper<Forecast, List<ForecastItem>> {
    private static final DateTimeFormatter inputFormatter = forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter outputFormatter = forPattern("dd.MM.yyyy HH:mm");
    private static final String DEGREE  = "\u00b0";

    @Override
    public List<ForecastItem> map(Forecast source) {
        if (source == null || source.getInfo() == null || source.getInfo().isEmpty()) {
            return Collections.emptyList();
        }
        return FluentIterable.from(source.getInfo())
                .transform(input -> {
                    Main main = input.getMain();
                    Weather weather = input.getWeather().get(0);
                    long temp = round(main.getTemp());
                    String tempStr = valueOf(temp) + DEGREE;
                    return ForecastItem.builder()
                            .date(outputFormatter.print(inputFormatter.parseDateTime(input.getDtTxt())))
                            .weather(weather.getIcon() + ".png")
                            .weatherDesc(weather.getDescription())
                            .temp(temp > 0 ? "+" + tempStr : tempStr)
                            .wind(valueOf(round(input.getWind().getSpeed())) + " м/c")
                            .humidity(valueOf(main.getHumidity()) + "%")
                            .pressure(valueOf(round(main.getGrndLevel())) + " гПа")
                            .build();
                })
                .toList();
    }
}
