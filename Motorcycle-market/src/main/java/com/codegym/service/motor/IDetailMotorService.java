package com.codegym.service.motor;

import com.codegym.service.IGeneralService;
import com.codegym.service.dto.ColorMotorDTO;
import com.codegym.service.dto.DetailMotorDTO;
import com.codegym.service.dto.ModelYearDTO;


public interface IDetailMotorService extends IGeneralService<DetailMotorDTO> {
    DetailMotorDTO getByModelYearAndColorMotor(ModelYearDTO modelYearDTO, ColorMotorDTO colorMotorDTO);

}


