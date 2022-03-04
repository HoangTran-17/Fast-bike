package com.motomarket.service.motor;

import com.motomarket.repository.IColorMotorRepository;
import com.motomarket.repository.IDetailMotorRepository;
import com.motomarket.repository.IModelYearRepository;
import com.motomarket.repository.model.ColorMotor;
import com.motomarket.repository.model.DetailMotor;
import com.motomarket.repository.model.ModelYear;
import com.motomarket.service.dto.ColorMotorDTO;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ModelYearDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailMotorService implements IDetailMotorService{
    @Autowired
    private IDetailMotorRepository detailMotorRepository;
    @Autowired
    private IModelYearRepository modelYearRepository;
    @Autowired
    private IColorMotorRepository colorMotorRepository;


    @Override
    public DetailMotorDTO getByModelYearAndColorMotor(ModelYearDTO modelYearDTO, ColorMotorDTO colorMotorDTO) {
        ModelYear modelYear = modelYearRepository.getById(modelYearDTO.getModelYearId());
        ColorMotor colorMotor = colorMotorRepository.getById(colorMotorDTO.getColorId());

        DetailMotor detailMotor = detailMotorRepository.getByModelYearAndColorMotor(modelYear, colorMotor);

        if (detailMotor == null) {
//            DetailMotor detailMotor1 = new DetailMotor();
//            detailMotorRepository.save(detailMotor1);
        }

        return new DetailMotorDTO(detailMotor.getDetailMotorId());
    }
    @Override
    public List<DetailMotorDTO> findAll() {
        return null;
    }

    @Override
    public DetailMotorDTO getById(Long id) {
        return null;
    }

    @Override
    public DetailMotor getDetailMotorById(Long id) {
        return detailMotorRepository.getById(id);
    }

    @Override
    public DetailMotorDTO save(DetailMotorDTO detailMotorDTO) {
        return null;
    }

    @Override
    public void remove(Long id) {

    }


}
