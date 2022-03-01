package com.motomarket.controlller.restful;

import com.motomarket.repository.model.ModelYear;
import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.dto.BrandMotorDTO;
import com.motomarket.service.dto.ColorMotorDTO;
import com.motomarket.service.dto.ModelYearDTO;
import com.motomarket.service.dto.SeriesMotorDTO;
import com.motomarket.service.motor.IBrandMotorService;
import com.motomarket.service.motor.IColorMotorService;
import com.motomarket.service.motor.IModelYearService;
import com.motomarket.service.motor.ISeriesMotorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/moto")
public class MotoAPI {

    @Autowired
    private IBrandMotorService brandMotorService;

    @Autowired
    private ISeriesMotorService seriesMotorService;

    @Autowired
    private IModelYearService modelYearService;

    @Autowired
    private IColorMotorService colorMotorService;

    @GetMapping("/getAllBrand")
    public ResponseEntity<List<BrandMotorDTO>> getAllBrand(){
       List<BrandMotorDTO> brandMotors;
        brandMotors = brandMotorService.findAll();
        return new ResponseEntity<>(brandMotors, HttpStatus.OK);
    }

    @GetMapping("/getSeries/{id}")
    public ResponseEntity<List<SeriesMotorDTO>> getSeriesByBrand(@PathVariable Long id){
        BrandMotorDTO brandMotorDTO = brandMotorService.getById(id);
        List<SeriesMotorDTO> seriesMotors = seriesMotorService.findAllByBrandMotorDTO(brandMotorDTO);
        System.out.println(seriesMotors);
        return new ResponseEntity<>(seriesMotors, HttpStatus.OK);
    }

    @GetMapping("/getModelYears/{id}")
    public ResponseEntity<List<ModelYearDTO>> getModelYearsBySeriesId(@PathVariable Long id){
        SeriesMotorDTO seriesMotor = seriesMotorService.getById(id);
        System.out.println(seriesMotor);
        List<ModelYearDTO> modelYearList = modelYearService.findAllBySeriesMotorDTO(seriesMotor);
        return new ResponseEntity<>(modelYearList, HttpStatus.OK);
    }

    @GetMapping("/getColors")
    public ResponseEntity<List<ColorMotorDTO>> getColors(){
        List<ColorMotorDTO> colorMotorDTOList = colorMotorService.findAll();
        return new ResponseEntity<>(colorMotorDTOList, HttpStatus.OK);
    }

}
