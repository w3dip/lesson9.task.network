package ru.sberbank.lesson9.task.network.domain.interactor;

public interface Callback<T> {
    void handle(T value);
}
