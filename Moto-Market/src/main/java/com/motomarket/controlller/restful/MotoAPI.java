package com.motomarket.controlller.restful;

import com.motomarket.service.dto.BrandMotorDTO;
import com.motomarket.service.motor.IBrandMotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MotoAPI {

    @Autowired
    private IBrandMotorService brandMotorService;

    @GetMapping("/getAllBrand")
    public ResponseEntity<List<BrandMotorDTO>> getAllBrand(){
       List<BrandMotorDTO> brandMotors;
        brandMotors = brandMotorService.findAll();
        return new ResponseEntity<>(brandMotors, HttpStatus.OK);
    }

}
