package com.motomarket.service.motor;

import com.motomarket.repository.ITypeMotorRepository;
import com.motomarket.repository.model.TypeMotor;
import com.motomarket.service.dto.TypeMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeMotorService implements ITypeMotorService{
    @Autowired
    private ITypeMotorRepository typeMotorRepository;

    @Override
    public List<TypeMotorDTO> findAll() {
        List<TypeMotorDTO> typeMotorDTOList = new ArrayList<>();
        typeMotorRepository.findAll().forEach(typeMotor -> {
            typeMotorDTOList.add(TypeMotorDTO.parseTypeMotorDTO(typeMotor));
        });
        return typeMotorDTOList;
    }

    @Override
    public TypeMotorDTO getById(Long id) {
        TypeMotor typeMotor = typeMotorRepository.getById(id);
        return TypeMotorDTO.parseTypeMotorDTO(typeMotor);
    }

    @Override
    public TypeMotorDTO save(TypeMotorDTO typeMotorDTO) {
        TypeMotor newTypeMotor = typeMotorRepository.save(TypeMotorDTO.parseTypeMotor(typeMotorDTO));
        return TypeMotorDTO.parseTypeMotorDTO(newTypeMotor);
    }

    @Override
    public void remove(Long id) {
    }
}
