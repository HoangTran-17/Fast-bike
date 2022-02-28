package com.motomarket.service.motor;

import com.motomarket.repository.IColorMotorRepository;
import com.motomarket.service.dto.ColorMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColorMotorService implements IColorMotorService {
    @Autowired
    private IColorMotorRepository colorMotorRepository;


    @Override
    public List<ColorMotorDTO> findAll() {
        List<ColorMotorDTO> colorMotorDTOList = new ArrayList<>();
        colorMotorRepository.findAll().forEach(colorMotor -> {
            colorMotorDTOList.add(ColorMotorDTO.parseColorMotorDTO(colorMotor));
        });
        return colorMotorDTOList;
    }

    @Override
    public ColorMotorDTO getById(Long id) {
        return null;
    }

    @Override
    public ColorMotorDTO save(ColorMotorDTO colorMotorDTO) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }
}
