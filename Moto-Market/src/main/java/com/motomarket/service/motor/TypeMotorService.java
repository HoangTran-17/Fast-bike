package com.motomarket.service.motor;

import com.motomarket.repository.ITypeMotorRepository;
import com.motomarket.repository.model.TypeMotor;
import com.motomarket.service.dto.TypeMotorDTO;
import com.motomarket.service.filter.BrandFilter;
import com.motomarket.service.filter.TypeMotorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeMotorService implements ITypeMotorService{
    @Autowired
    private ITypeMotorRepository typeMotorRepository;

    @Override
    public List<TypeMotorDTO> findAll() {
        List<TypeMotorDTO> typeMotorDTOList = new ArrayList<>();
        typeMotorRepository.findAll().forEach(typeMotor -> {
            typeMotorDTOList.add(TypeMotorDTO.parseTypeMotorDTO(typeMotor));
        });
        return typeMotorDTOList;
    }

    @Override
    public TypeMotorDTO getById(Long id) {
        TypeMotor typeMotor = typeMotorRepository.getById(id);
        return TypeMotorDTO.parseTypeMotorDTO(typeMotor);
    }

    @Override
    public TypeMotorDTO save(TypeMotorDTO typeMotorDTO) {
        TypeMotor newTypeMotor = typeMotorRepository.save(TypeMotorDTO.parseTypeMotor(typeMotorDTO));
        return TypeMotorDTO.parseTypeMotorDTO(newTypeMotor);
    }

    @Override
    public void remove(Long id) {
    }

    @Override
    public List<TypeMotorFilter> getAllTypeMotorFilter(String br, String tp, String cc) {
        List<TypeMotorFilter> typeMotorFilterList = new ArrayList<>();
        typeMotorRepository.findAll().forEach(typeMotor -> {
            String href = setHref(br, tp, cc, typeMotor.getTypeMotorId());
            Boolean bo = isSelected(tp,typeMotor.getTypeMotorId());
            typeMotorFilterList.add(TypeMotorFilter.parseTypeMotorFilter(typeMotor,href,bo));
        });
        return typeMotorFilterList;
    }

    private Boolean isSelected(String tp, Long typeMotorId) {
        if (tp == null) {
            return false;
        }
        List<String> list = new ArrayList<>(List.of(tp.split("_")));
        int i = list.indexOf(String.valueOf(typeMotorId));
        return i != -1;
    }

    private String setHref(String br, String tp, String cc, Long typeMotorId) {
        StringBuilder href = new StringBuilder();
        if (br!=null) {
            href.append("br=");
            href.append(br);
            href.append("&");
        }
        if (tp == null) {
            href.append("tp=");
            href.append(typeMotorId);
            href.append("&");
        }
        else if (!tp.equals(typeMotorId.toString())) {
            href.append("tp=");
            List<String> list = new ArrayList<>(List.of(tp.split("_")));
            int i = list.indexOf(String.valueOf(typeMotorId));
            if (i == -1) {
                list.add(String.valueOf(typeMotorId));
            } else {
                list.remove(i);
            }
            tp = String.join("_", list);
            href.append(tp);
            href.append("&");

        }
        if (cc!=null) {
            href.append("cc=");
            href.append(cc);
            href.append("&");
        }
        return String.valueOf(href);
    }
}
