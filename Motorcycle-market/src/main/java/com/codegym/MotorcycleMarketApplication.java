package com.codegym;

import com.codegym.repository.IColorMotorRepository;
import com.codegym.repository.IDetailMotorRepository;
import com.codegym.repository.IModelYearRepository;
import com.codegym.repository.ISeriesMotorRepository;
import com.codegym.repository.model.*;
import com.codegym.service.dto.*;
import com.codegym.service.location.IDistrictService;
import com.codegym.service.location.IProvinceService;
import com.codegym.service.motor.*;
import com.codegym.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class MotorcycleMarketApplication implements CommandLineRunner {
    @Autowired
    private IBrandMotorService brandMotorService;

    @Autowired
    private ISeriesMotorRepository seriesMotorRepository;

    @Autowired
    private IModelYearRepository modelYearRepository;

    @Autowired
    private IColorMotorRepository colorMotorRepository;

    @Autowired
    private IDetailMotorRepository detailMotorRepository;

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private IDistrictService districtService;

    @Autowired
    private IPostService postService;


    public static void main(String[] args) {
        SpringApplication.run(MotorcycleMarketApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {







    }

//    private void insertData() {
//        SeriesMotor seriesMotor = seriesMotorRepository.getById(12L);
//
//        for (int i = 2018; i < 2022; i++) {
//            ModelYear modelYear = new ModelYear();
//            modelYear.setSeriesMotor(seriesMotor);
//            modelYear.setModelYearName(i);
//            modelYearRepository.save(modelYear);
//        }
//
//
//
//        modelYearRepository.getAllBySeriesMotor(seriesMotor).forEach(modelYear -> {
//            colorMotorRepository.findAll().forEach(colorMotor -> {
//                DetailMotor detailMotor = new DetailMotor();
//                detailMotor.setBrandMotor(seriesMotor.getBrandMotor());
//                detailMotor.setSeriesMotor(seriesMotor);
//                detailMotor.setTypeMotor(seriesMotor.getTypeMotor());
//                detailMotor.setModelYear(modelYear);
//                detailMotor.setColorMotor(colorMotor);
//                detailMotorRepository.save(detailMotor);
//            });
//        });
//    }
}
