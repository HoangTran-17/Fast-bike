package com.motomarket.service.motor;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.TypeMotorDTO;
import com.motomarket.service.filter.TypeMotorFilter;

import java.util.List;


public interface ITypeMotorService extends IGeneralService<TypeMotorDTO> {

    List<TypeMotorFilter> getAllTypeMotorFilter(String modelMotor, String br, String tp, String cc);
}

