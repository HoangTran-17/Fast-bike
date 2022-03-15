package com.motomarket.service.motor;

import com.motomarket.repository.ITypeMotorRepository;
import com.motomarket.repository.model.TypeMotor;
import com.motomarket.service.dto.TypeMotorDTO;
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
    public List<TypeMotorFilter> getAllTypeMotorFilter(String modelMotor, String br, String tp, String cc, Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax, String kilometerCount, String color, String province) {
        List<TypeMotorFilter> typeMotorFilterList = new ArrayList<>();
        typeMotorRepository.findAll().forEach(typeMotor -> {
            String href = setHref(modelMotor,br, tp, cc,priceFrom,priceTo,modelYearMin,modelYearMax,kilometerCount,color,province, typeMotor.getTypeMotorId());
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

    private String setHref(String modelMotor, String br, String tp, String cc, Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax, String kilometerCount, String color, String province, Long typeMotorId) {
        StringBuilder href = new StringBuilder();
        if (modelMotor!=null) {
            href.append("q=");
            href.append(modelMotor);
            href.append("&");
        }
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
        return getString(priceFrom, priceTo, modelYearMin, modelYearMax, kilometerCount, color, province, href);
    }

    public static String getString(Double priceFrom, Double priceTo, Integer modelYearMin, Integer modelYearMax, String kilometerCount, String color, String province, StringBuilder href) {

        if (priceFrom!=null) {
            href.append("pr-fr=");
            href.append(priceFrom);
            href.append("&");
        }
        if (priceTo!=null) {
            href.append("pr-to=");
            href.append(priceTo);
            href.append("&");
        }
        if (modelYearMin!=null) {
            href.append("my-fr=");
            href.append(modelYearMin);
            href.append("&");
        }
        if (modelYearMax!=null) {
            href.append("my-to=");
            href.append(modelYearMax);
            href.append("&");
        }
        if (kilometerCount!=null) {
            href.append("km=");
            href.append(kilometerCount);
            href.append("&");
        }
        if (color!=null) {
            href.append("color=");
            href.append(color);
            href.append("&");
        }
        if (province!=null) {
            href.append("pr=");
            href.append(province);
            href.append("&");
        }
        return String.valueOf(href);
    }
}
