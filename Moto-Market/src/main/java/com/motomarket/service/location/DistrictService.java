package com.motomarket.service.location;

import com.motomarket.repository.IDistrictRepository;
import com.motomarket.repository.IProvinceRepository;
import com.motomarket.repository.model.Province;
import com.motomarket.service.dto.DistrictDTO;
import com.motomarket.service.dto.ProvinceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictService implements IDistrictService{
    @Autowired
    private IDistrictRepository districtRepository;
    @Autowired
    private IProvinceRepository provinceRepository;


    @Override
    public List<DistrictDTO> getAllByProvinceDTO(ProvinceDTO provinceDTO) {
        Province province = provinceRepository.getById(provinceDTO.getProvinceId());
        List<DistrictDTO> districtDTOList = new ArrayList<>();
        districtRepository.getAllByProvince(province).forEach(district -> {
            districtDTOList.add(DistrictDTO.parseDistrictDTO(district));
        });
        return districtDTOList;
    }

    @Override
    public List<DistrictDTO> findAll() {
        return null;
    }

    @Override
    public DistrictDTO getById(Long id) {
        return null;
    }

    @Override
    public DistrictDTO save(DistrictDTO districtDTO) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }

}
