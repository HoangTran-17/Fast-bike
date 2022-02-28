package com.motomarket.service.motor;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.ColorMotorDTO;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ModelYearDTO;


public interface IDetailMotorService extends IGeneralService<DetailMotorDTO> {
    DetailMotorDTO getByModelYearAndColorMotor(ModelYearDTO modelYearDTO, ColorMotorDTO colorMotorDTO);

}


