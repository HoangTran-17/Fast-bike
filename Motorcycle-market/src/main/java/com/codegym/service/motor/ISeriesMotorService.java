package com.codegym.service.motor;

import com.codegym.repository.model.SeriesMotor;
import com.codegym.service.IGeneralService;
import com.codegym.service.dto.BrandMotorDTO;
import com.codegym.service.dto.SeriesMotorDTO;

import java.util.List;

public interface ISeriesMotorService extends IGeneralService<SeriesMotorDTO> {
    List<SeriesMotorDTO> findAllByBrandMotorDTO(BrandMotorDTO brandMotorDTO);

    SeriesMotor parseSeriesMotor(SeriesMotorDTO seriesMotorDTO);
}

