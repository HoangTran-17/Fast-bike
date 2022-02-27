package com.codegym.service;

import com.codegym.service.dto.BrandMotorDTO;

import java.util.List;
import java.util.Optional;

public interface IGeneralService<T> {
    List<T> findAll();

    T getById(Long id);

    T save(T t);

    void remove(Long id);
}
