package com.motomarket.service.motor;

import com.motomarket.repository.*;
import com.motomarket.repository.model.*;
import com.motomarket.service.dto.ColorMotorDTO;
import com.motomarket.service.dto.DetailMotorDTO;
import com.motomarket.service.dto.ModelYearDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetailMotorService implements IDetailMotorService{
    @Autowired
    private IBrandMotorRepository brandMotorRepository;
    @Autowired
    private IDetailMotorRepository detailMotorRepository;
    @Autowired
    private IModelYearRepository modelYearRepository;
    @Autowired
    private IColorMotorRepository colorMotorRepository;
    @Autowired
    private ISeriesMotorRepository seriesMotorRepository;
    @Autowired
    private IModelYearService modelYearService;
    @Autowired
    private IColorMotorService colorMotorService;



    @Override
    public DetailMotorDTO getByModelYearAndColorMotor(Long modelYearId, Long colorId) {
        ModelYear modelYear = modelYearRepository.getById(modelYearId);
        ColorMotor colorMotor = colorMotorRepository.getById(colorId);

        DetailMotor detailMotor = detailMotorRepository.getByModelYearAndColorMotor(modelYear, colorMotor);

        if (detailMotor == null) {
            detailMotor.setBrandMotor(modelYear.getSeriesMotor().getBrandMotor());
            detailMotor.setSeriesMotor(modelYear.getSeriesMotor());
            detailMotor.setColorMotor(colorMotor);
            detailMotor.setModelYear(modelYear);
            detailMotor.setTypeMotor(modelYear.getSeriesMotor().getTypeMotor());
            detailMotorRepository.save(detailMotor);
        }

        return DetailMotorDTO.parseDetailMotorDTO(detailMotor);
    }
    @Override
    public List<DetailMotorDTO> findAll() {
        List<DetailMotorDTO> list = new ArrayList<>();
        detailMotorRepository.findAll().forEach(detailMotor -> {
            list.add(DetailMotorDTO.parseDetailMotorDTO(detailMotor));
        });
        return list;
    }

    @Override
    public DetailMotorDTO getById(Long id) {
        return DetailMotorDTO.parseDetailMotorDTO(detailMotorRepository.getById(id));
    }

    @Override
    public DetailMotor getDetailMotorById(Long id) {
        return detailMotorRepository.getById(id);
    }

    @Override
    public DetailMotorDTO save(DetailMotorDTO detailMotorDTO) {
//        DetailMotor newDetailMotor = new DetailMotor();
//        BrandMotor brandMotor = brandMotorRepository.getByBrandName(detailMotorDTO.getBrandMotor().getBrandName());
//
//        SeriesMotor seriesMotor = seriesMotorRepository.getById(detailMotorDTO.getSeriesMotor().getSeriesId());
//        ColorMotor colorMotor = colorMotorRepository.getById(detailMotorDTO.getColorMotor().getColorId());
//

        return null;
    }

    @Override
    public void remove(Long id) {

    }


}
