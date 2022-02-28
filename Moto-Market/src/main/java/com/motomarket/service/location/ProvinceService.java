package com.motomarket.service.location;

import com.motomarket.repository.IProvinceRepository;
import com.motomarket.service.dto.ProvinceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinceService implements IProvinceService{
    @Autowired
    private IProvinceRepository provinceRepository;

    @Override
    public List<ProvinceDTO> findAll() {
        List<ProvinceDTO> provinceDTOList = new ArrayList<>();
         provinceRepository.findAll().forEach(province -> {
             provinceDTOList.add(ProvinceDTO.parseProvinceDTO(province));
         });

         return provinceDTOList;
    }

    @Override
    public ProvinceDTO getById(Long id) {
        return null;
    }

    @Override
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
