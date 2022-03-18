package com.motomarket.service.motor;

import com.motomarket.repository.model.Specifications;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.SpecificationsDTO;

public interface ISpecificationsService extends IGeneralService<Specifications> {
    Specifications save(SpecificationsDTO specificationsDTO);
}
