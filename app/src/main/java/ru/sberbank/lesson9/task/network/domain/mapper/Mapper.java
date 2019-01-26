package ru.sberbank.lesson9.task.network.domain.mapper;

public interface Mapper<T,V> {
    V map(T from);
}
