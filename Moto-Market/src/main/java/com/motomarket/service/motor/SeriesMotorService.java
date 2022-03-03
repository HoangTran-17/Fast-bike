package com.motomarket.service.motor;

import com.motomarket.repository.IBrandMotorRepository;
import com.motomarket.repository.ISeriesMotorRepository;
import com.motomarket.repository.model.BrandMotor;
import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.repository.model.Specifications;
import com.motomarket.repository.model.TypeMotor;
import com.motomarket.service.dto.BrandMotorDTO;
import com.motomarket.service.dto.SeriesMotorDTO;
import com.motomarket.service.dto.SpecificationsDTO;
import com.motomarket.service.dto.TypeMotorDTO;
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
        SeriesMotor seriesMotor = new SeriesMotor();
        seriesMotor.setSeriesId(seriesMotorDTO.getSeriesId());
        seriesMotor.setSeriesName(seriesMotorDTO.getSeriesName());
        seriesMotor.setCapacity(seriesMotorDTO.getCapacity());
        seriesMotor.setBrandMotor(seriesMotor.getBrandMotor());
        TypeMotor typeMotor = TypeMotorDTO.parseTypeMotor(seriesMotorDTO.getTypeMotorDTO());
        seriesMotor.setTypeMotor(typeMotor);
        Specifications specifications = SpecificationsDTO.parseSpecifications(seriesMotorDTO.getSpecificationsDTO());
        seriesMotor.setSpecifications(specifications);

        SeriesMotor newSeriesMotor = seriesMotorRepository.save(seriesMotor);
        return SeriesMotorDTO.parseSeriesMotorDTO(newSeriesMotor);
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
