package com.motomarket.service.motor;

import com.motomarket.service.IGeneralService;
import com.motomarket.service.filter.BrandFilter;
import com.motomarket.service.dto.BrandMotorDTO;

import java.util.List;


public interface IBrandMotorService extends IGeneralService<BrandMotorDTO> {

    List<BrandFilter> getAllBrandFilter(String modelMotor, String br, String tp, String cc,
                                        Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax,
                                        String kilometerCount, String color, String province);
<<<<<<< HEAD

    List<BrandFilter> getAllBrandFilter(String modelMotor, String br, String tp, String cc);
    BrandMotorDTO getBrandByBrandName(String brandName);

=======
    List<BrandFilter> getAllBrandFilter(String modelMotor, String br, String tp, String cc);
    BrandMotorDTO getBrandByBrandName(String brandName);
>>>>>>> tien-dev
}

