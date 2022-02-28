package com.motomarket.service.location;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.dto.DistrictDTO;
import com.motomarket.service.dto.ProvinceDTO;

import java.util.List;

public interface IDistrictService extends IGeneralService<DistrictDTO> {
    List<DistrictDTO> getAllByProvinceDTO(ProvinceDTO provinceDTO);
}

