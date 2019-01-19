package ru.sberbank.lesson9.task.network.presentation.mapper;

public interface Mapper<T,V> {
    V map(T from);
}
