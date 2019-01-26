package ru.sberbank.lesson9.task.network.presentation.view.activity;

import ru.sberbank.lesson9.task.network.domain.entity.ForecastEntity;

public interface BaseView {
    void handle(ForecastEntity forecast);
}
