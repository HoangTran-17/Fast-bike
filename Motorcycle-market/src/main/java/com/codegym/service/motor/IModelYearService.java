package com.codegym.service.motor;

import com.codegym.service.IGeneralService;
import com.codegym.service.dto.BrandMotorDTO;
import com.codegym.service.dto.ModelYearDTO;
import com.codegym.service.dto.SeriesMotorDTO;

import java.util.List;

public interface IModelYearService extends IGeneralService<ModelYearDTO> {
    public List<ModelYearDTO> findAllBySeriesMotorDTO(SeriesMotorDTO seriesMotorDTO);
}

