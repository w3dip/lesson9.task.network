package ru.sberbank.lesson9.task.network.presentation.view;

import ru.sberbank.lesson9.task.network.domain.model.ForecastItem;

public interface BaseView {
    void handle(ForecastItem forecast);
}
