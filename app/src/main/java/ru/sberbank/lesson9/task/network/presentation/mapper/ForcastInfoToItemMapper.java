package ru.sberbank.lesson9.task.network.presentation.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.SneakyThrows;
import ru.sberbank.lesson9.task.network.domain.model.Info;
import ru.sberbank.lesson9.task.network.domain.model.Main;
import ru.sberbank.lesson9.task.network.domain.model.Weather;
import ru.sberbank.lesson9.task.network.presentation.model.ForecastItem;

public class ForcastInfoToItemMapper implements Mapper<Info, ForecastItem> {
    private static final String DATE_FORMAT_INPUT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_OUTPUT = "dd.MM.yyyy HH:mm";
    private static final String DEGREE  = "\u00b0";

    @Override
    @SneakyThrows(ParseException.class)
    public ForecastItem map(Info from) {
        Main main = from.getMain();
        Weather weather = from.getWeather().get(0);
        SimpleDateFormat inputFormatter = new SimpleDateFormat(DATE_FORMAT_INPUT, Locale.getDefault());
        SimpleDateFormat outputFormatter = new SimpleDateFormat(DATE_FORMAT_OUTPUT, Locale.getDefault());
        Date dateTime = inputFormatter.parse(from.getDtTxt());

        long temp = Math.round(main.getTemp());
        String tempStr = String.valueOf(temp) + DEGREE;
        return ForecastItem.builder()
                .date(outputFormatter.format(dateTime))
                .weather(weather.getIcon() + ".png")
                .weatherDesc(weather.getDescription())
                .temp(temp > 0 ? "+" + tempStr : tempStr)
                .build();
    }
}
