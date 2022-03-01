package com.motomarket.service.motor;

import com.motomarket.repository.IBrandMotorRepository;
import com.motomarket.repository.model.BrandMotor;
import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.dto.BrandMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandMotorService implements IBrandMotorService {
    @Autowired
    private IBrandMotorRepository brandMotorRepository;


    @Override
    public List<BrandMotorDTO> findAll() {
        List<BrandMotorDTO> brandMotors = new ArrayList<>();
//        List<BrandMotor> list = brandMotorRepository.findAll();
//        for (BrandMotor brandMotor:list) {
//            brandMotors.add( BrandMotorDTO.parseBrandMotorDTO(brandMotor));
//
//        }
        brandMotorRepository.findAll().forEach(brandMotor -> {
            brandMotors.add(BrandMotorDTO.parseBrandMotorDTO(brandMotor));
        });
        return brandMotors;
    }

    @Override
    public BrandMotorDTO getById(Long id) {
        BrandMotor brandMotor = brandMotorRepository.getById(id);
        return BrandMotorDTO.parseBrandMotorDTO(brandMotor);
    }

    @Override
    public BrandMotorDTO save(BrandMotorDTO brandMotorDTO) {
//        BrandMotor brandMotor = new BrandMotor();
//
//        brandMotor.setBrandId(brandMotorDTO.getBrandId());
//        brandMotor.setBrandName(brandMotorDTO.getBrandName());
//
//        brandMotorRepository.save(brandMotor);

        return brandMotorDTO;
    }

    @Override
    public void remove(Long id) {

    }

    BrandMotor parseBrandMotor(BrandMotorDTO brandMotorDTO) {
        return new BrandMotor(brandMotorDTO.getBrandId(), brandMotorDTO.getBrandName());
    }


}
