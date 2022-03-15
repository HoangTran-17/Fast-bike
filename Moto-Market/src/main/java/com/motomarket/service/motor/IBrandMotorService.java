package com.motomarket.service.motor;

import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.filter.BrandFilter;
import com.motomarket.service.dto.BrandMotorDTO;

import java.util.List;


public interface IBrandMotorService extends IGeneralService<BrandMotorDTO> {

    List<BrandFilter> getAllBrandFilter(String br, String tp, String cc);
}

