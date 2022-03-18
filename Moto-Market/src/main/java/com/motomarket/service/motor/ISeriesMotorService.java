package com.motomarket.service.motor;

import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.BrandMotorDTO;
import com.motomarket.service.dto.SeriesMotorDTO;

import java.util.List;

public interface ISeriesMotorService extends IGeneralService<SeriesMotorDTO> {
    List<SeriesMotorDTO> findAllByBrandMotorDTO(BrandMotorDTO brandMotorDTO);

    SeriesMotor parseSeriesMotor(SeriesMotorDTO seriesMotorDTO);

    SeriesMotorDTO getBySeriesName(String seriesName);
}

