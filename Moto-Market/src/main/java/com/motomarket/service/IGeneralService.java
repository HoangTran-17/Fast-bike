package com.motomarket.service;

import java.util.List;

public interface IGeneralService<T> {
    List<T> findAll();

    T getById(Long id);

    T save(T t);

    void remove(Long id);
}
