package com.motomarket.service.motor;

import com.motomarket.repository.IModelYearRepository;
import com.motomarket.repository.ISeriesMotorRepository;
import com.motomarket.repository.model.ModelYear;
import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.service.dto.ModelYearDTO;
import com.motomarket.service.dto.SeriesMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelYearService implements IModelYearService{
    @Autowired
    private IModelYearRepository modelYearRepository;

    @Autowired
    private ISeriesMotorRepository seriesMotorRepository;

    @Autowired
    private ISeriesMotorService seriesMotorService;


    @Override
    public List<ModelYearDTO> findAllBySeriesMotorDTO(SeriesMotorDTO seriesMotorDTO) {
        SeriesMotor seriesMotor = seriesMotorRepository.getById(seriesMotorDTO.getSeriesId());

        List<ModelYearDTO> modelYearDTOList = new ArrayList<>();
        modelYearRepository.getAllBySeriesMotor(seriesMotor).forEach(modelYear -> {
            modelYearDTOList.add(ModelYearDTO.parseModelYearDTO(modelYear));
        });

        return modelYearDTOList;
    }
    @Override
    public List<ModelYearDTO> findAll() {
        return null;
    }

    @Override
    public ModelYearDTO getById(Long id) {
        return null;
    }

    @Override
    public ModelYearDTO save(ModelYearDTO modelYearDTO) {
        ModelYear modelYear = modelYearRepository.save(parseModelYear(modelYearDTO));
        return new ModelYearDTO();
    }

    private ModelYear parseModelYear(ModelYearDTO modelYearDTO) {
        SeriesMotor seriesMotor = seriesMotorService.parseSeriesMotor(modelYearDTO.getSeriesMotorDTO());
        return new ModelYear(modelYearDTO.getModelYearId(), modelYearDTO.getModelYearName(),
                seriesMotor, new ArrayList<>());
    }

    @Override
    public void remove(Long id) {

    }


}
