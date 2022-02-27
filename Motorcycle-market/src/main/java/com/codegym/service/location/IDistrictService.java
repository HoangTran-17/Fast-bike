package com.codegym.service.location;

import com.codegym.service.IGeneralService;
import com.codegym.service.dto.DistrictDTO;
import com.codegym.service.dto.ProvinceDTO;

import java.util.List;

public interface IDistrictService extends IGeneralService<DistrictDTO> {
    List<DistrictDTO> getAllByProvinceDTO(ProvinceDTO provinceDTO);
}

