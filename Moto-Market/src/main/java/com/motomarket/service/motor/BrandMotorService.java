package com.motomarket.service.motor;

import com.motomarket.repository.IBrandMotorRepository;
import com.motomarket.repository.model.BrandMotor;
import com.motomarket.service.filter.BrandFilter;
import com.motomarket.service.dto.BrandMotorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandMotorService implements IBrandMotorService {
    @Autowired
    private IBrandMotorRepository brandMotorRepository;


    @Override
    public List<BrandMotorDTO> findAll() {
        List<BrandMotorDTO> brandMotors = new ArrayList<>();
        brandMotorRepository.findAll().forEach(brandMotor -> {
            brandMotors.add(BrandMotorDTO.parseBrandMotorDTO(brandMotor));
        });
        return brandMotors;
    }

    @Override
    public BrandMotorDTO getById(Long id) {
        Optional<BrandMotor> brandMotor = brandMotorRepository.findById(id);
        return BrandMotorDTO.parseBrandMotorDTO(brandMotor.get());
    }

    @Override
    public BrandMotorDTO getBrandByBrandName(String brandName) {
        BrandMotor brandMotor = brandMotorRepository.getByBrandName(brandName);
        if (brandMotor==null) {
            return null;
        }
        return BrandMotorDTO.parseBrandMotorDTO(brandMotor);
    }

    @Override
    public BrandMotorDTO save(BrandMotorDTO brandMotorDTO) {
        BrandMotor brandMotor = new BrandMotor();
        brandMotor.setBrandId(brandMotorDTO.getBrandId());
        brandMotor.setBrandName(brandMotorDTO.getBrandName());
        brandMotorRepository.save(brandMotor);
        return brandMotorDTO;
    }

    @Override
    public void remove(Long id) {
        brandMotorRepository.deleteById(id);
    }

    BrandMotor parseBrandMotor(BrandMotorDTO brandMotorDTO) {
        return new BrandMotor(brandMotorDTO.getBrandId(), brandMotorDTO.getBrandName());
    }


    @Override
    public List<BrandFilter> getAllBrandFilter(String modelMotor, String br, String tp, String cc) {
        List<BrandFilter> brandFilterList = new ArrayList<>();
        brandMotorRepository.findAll().forEach(brandMotor -> {
            String href = setHref(modelMotor,br, tp, cc, brandMotor.getBrandId());
            Boolean bo = isSelected(br,brandMotor.getBrandId());
            brandFilterList.add(BrandFilter.parseBrandFilter(brandMotor,href,bo));
        });
        return brandFilterList;
    }

    private Boolean isSelected(String br, Long brandId) {
        if (br == null) {
            return false;
        }
        List<String> list = new ArrayList<>(List.of(br.split("_")));
        int i = list.indexOf(String.valueOf(brandId));
        return i != -1;
    }

    private String setHref(String modelMotor,String br, String tp, String cc, Long brandId) {
        StringBuilder href = new StringBuilder();
        if (modelMotor!=null) {
            href.append("q=");
            href.append(modelMotor);
            href.append("&");
        }
        if (br == null) {
            href.append("br=");
            href.append(brandId);
            href.append("&");
        }
        else if (!br.equals(brandId.toString())) {
            href.append("br=");
            List<String> list = new ArrayList<>(List.of(br.split("_")));
            int i = list.indexOf(String.valueOf(brandId));
            if (i == -1) {
                list.add(String.valueOf(brandId));
            } else {
                list.remove(i);
            }
            br = String.join("_", list);
            href.append(br);
            href.append("&");

        }
        if (tp!=null) {
            href.append("tp=");
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
