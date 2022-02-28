package com.motomarket.service.motor;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.ModelYearDTO;
import com.motomarket.service.dto.SeriesMotorDTO;

import java.util.List;

public interface IModelYearService extends IGeneralService<ModelYearDTO> {
    public List<ModelYearDTO> findAllBySeriesMotorDTO(SeriesMotorDTO seriesMotorDTO);
}

