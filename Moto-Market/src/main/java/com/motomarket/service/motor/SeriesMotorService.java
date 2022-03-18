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
import java.util.Optional;

@Service
public class SeriesMotorService implements ISeriesMotorService {

    @Autowired
    private ISeriesMotorRepository seriesMotorRepository;

    @Autowired
    private IBrandMotorRepository brandMotorRepository;

    @Autowired
    private BrandMotorService brandMotorService;

    @Autowired
    private ISpecificationsService specificationsService;

    @Override
    public List<SeriesMotorDTO> findAllByBrandMotorDTO(BrandMotorDTO brandMotorDTO) {
        Optional<BrandMotor> brandMotor = brandMotorRepository.findById(brandMotorDTO.getBrandId());
        List<SeriesMotorDTO> seriesMotorList = new ArrayList<>();
        seriesMotorRepository.getAllByBrandMotor(brandMotor.get()).forEach(seriesMotor -> {
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
    public SeriesMotorDTO getBySeriesName(String seriesName) {
       SeriesMotor seriesMotor = seriesMotorRepository.getBySeriesName(seriesName);
        if (seriesMotor == null) {
            return null;
        }
        return SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor);
    }

    @Override
    public SeriesMotorDTO save(SeriesMotorDTO seriesMotorDTO) {
        Optional<BrandMotor> brandMotor = brandMotorRepository.findById(seriesMotorDTO.getBrandId());
        SeriesMotor seriesMotor = new SeriesMotor();
        seriesMotor.setSeriesName(seriesMotorDTO.getSeriesName());
        seriesMotor.setCapacity(seriesMotorDTO.getCapacity());
        seriesMotor.setBrandMotor(brandMotor.get());
        TypeMotor typeMotor = TypeMotorDTO.parseTypeMotor(seriesMotorDTO.getTypeMotorDTO());
        seriesMotor.setTypeMotor(typeMotor);
        Specifications specifications = SpecificationsDTO.parseSpecifications(seriesMotorDTO.getSpecificationsDTO());
        specifications.setSeriesMotor(seriesMotor);
        seriesMotor.setSpecifications(specificationsService.save(specifications));
        brandMotor.get().getSeriesMotorList().add(seriesMotor);
        brandMotorRepository.save(brandMotor.get());
        seriesMotorRepository.save(seriesMotor);
        return SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor);
    }

    public SeriesMotorDTO create(SeriesMotorDTO seriesMotorDTO) {
        BrandMotor brandMotor = brandMotorRepository.getById(seriesMotorDTO.getBrandId());
        SeriesMotor seriesMotor = new SeriesMotor();
        seriesMotor.setSeriesName(seriesMotorDTO.getSeriesName());
        seriesMotor.setCapacity(seriesMotorDTO.getCapacity());
        seriesMotor.setBrandMotor(brandMotor);
        TypeMotor typeMotor = TypeMotorDTO.parseTypeMotor(seriesMotorDTO.getTypeMotorDTO());
        seriesMotor.setTypeMotor(typeMotor);
        Specifications specifications = specificationsService.save(seriesMotorDTO.getSpecificationsDTO());
        seriesMotor.setSpecifications(specifications);
        brandMotor.getSeriesMotorList().add(seriesMotor);
        brandMotorRepository.save(brandMotor);
        seriesMotorRepository.save(seriesMotor);
        return SeriesMotorDTO.parseSeriesMotorDTO(seriesMotor);
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
