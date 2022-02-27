package com.codegym.service.motor;

import com.codegym.repository.IBrandMotorRepository;
import com.codegym.repository.ISeriesMotorRepository;
import com.codegym.repository.model.BrandMotor;
import com.codegym.repository.model.SeriesMotor;
import com.codegym.service.dto.BrandMotorDTO;
import com.codegym.service.dto.SeriesMotorDTO;
import com.codegym.service.dto.SpecificationsDTO;
import com.codegym.service.dto.TypeMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeriesMotorService implements ISeriesMotorService {

    @Autowired
    private ISeriesMotorRepository seriesMotorRepository;

    @Autowired
    private IBrandMotorRepository brandMotorRepository;

    @Autowired
    private BrandMotorService brandMotorService;

    @Override
    public List<SeriesMotorDTO> findAllByBrandMotorDTO(BrandMotorDTO brandMotorDTO) {
        BrandMotor brandMotor = brandMotorRepository.getById(brandMotorDTO.getBrandId());
        List<SeriesMotorDTO> seriesMotorList = new ArrayList<>();
        seriesMotorRepository.getAllByBrandMotor(brandMotor).forEach(seriesMotor -> {
            seriesMotorList.add(SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor));
        });
        return seriesMotorList;
    }

    @Override
    public List<SeriesMotorDTO> findAll() {
        List<SeriesMotorDTO> seriesMotorDTOList = new ArrayList<>();
        seriesMotorRepository.findAll().forEach(seriesMotor -> {
            seriesMotorDTOList.add(SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor));
        });
        return seriesMotorDTOList;
    }

    @Override
    public SeriesMotorDTO getById(Long id) {
        SeriesMotor seriesMotor = seriesMotorRepository.getById(id);
        SeriesMotorDTO seriesMotorDTO = SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor);
        return seriesMotorDTO;
    }

    @Override
    public SeriesMotorDTO save(SeriesMotorDTO seriesMotorDTO) {
        return null;
    }

    @Override
    public SeriesMotor parseSeriesMotor(SeriesMotorDTO seriesMotorDTO) {
        BrandMotor brandMotor = brandMotorService.parseBrandMotor(brandMotorService.getById(seriesMotorDTO.getBrandId()));
        return new SeriesMotor(seriesMotorDTO.getSeriesId(), seriesMotorDTO.getSeriesName(),
                seriesMotorDTO.getCapacity(), brandMotor,
                new TypeMotorDTO().parseTypeMotor(seriesMotorDTO.getTypeMotorDTO()),
                new SpecificationsDTO().parseSpecifications(seriesMotorDTO.getSpecificationsDTO()),
                new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public void remove(Long id) {
    }


}
