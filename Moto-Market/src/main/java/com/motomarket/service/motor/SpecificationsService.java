package com.motomarket.service.motor;

import com.motomarket.repository.ISpecificationsRepository;
import com.motomarket.repository.model.Specifications;
import com.motomarket.service.dto.SpecificationsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecificationsService implements ISpecificationsService{

    @Autowired
    private ISpecificationsRepository specificationsRepository;
    @Override
    public List<Specifications> findAll() {
        List<Specifications> specificationsList = specificationsRepository.findAll();
        return specificationsList;
    }

    @Override
    public Specifications getById(Long id) {
        return specificationsRepository.getById(id);
    }

    @Override
    public Specifications save(Specifications specifications) {
        return specificationsRepository.save(specifications);
    }

    @Override
    public Specifications save(SpecificationsDTO specificationsDTO) {
        Specifications specifications = SpecificationsDTO.parseSpecifications(specificationsDTO);

        return specificationsRepository.save(specifications);
    }

    @Override
    public void remove(Long id) {

    }
}
