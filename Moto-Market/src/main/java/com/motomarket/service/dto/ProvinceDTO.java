package com.motomarket.service.dto;

import com.motomarket.repository.model.Province;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ProvinceDTO {
    private Long provinceId;

    @NotBlank(message = "Vui lòng nhập tên tỉnh!")
    private String provinceName;

    public static ProvinceDTO parseProvinceDTO(Province province) {
        return new ProvinceDTO(province.getProvinceId(), province.getProvinceName());
    }

}

