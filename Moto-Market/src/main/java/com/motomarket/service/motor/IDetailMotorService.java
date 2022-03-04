package com.motomarket.service.motor;

import com.motomarket.repository.model.DetailMotor;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.ColorMotorDTO;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ModelYearDTO;


public interface IDetailMotorService extends IGeneralService<DetailMotorDTO> {
//    DetailMotorDTO getByModelYearAndColorMotor(ModelYearDTO modelYearDTO, ColorMotorDTO colorMotorDTO);

    DetailMotorDTO getByModelYearAndColorMotor(Long modelYearId, Long colorId);

    DetailMotor getDetailMotorById(Long id);
}


