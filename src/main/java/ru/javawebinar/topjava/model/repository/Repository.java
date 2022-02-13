package ru.javawebinar.topjava.model.repository;

import java.util.List;

public interface Repository<T> {

    void save(T entity);

    T getById(int id);

    List<T> getAll();

    void update(int id, T entity);

    void delete(int id);
}
