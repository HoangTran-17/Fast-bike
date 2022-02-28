package com.motomarket.service.dto;

import com.motomarket.repository.model.BrandMotor;
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
public class BrandMotorDTO {

    private Long brandId;

    @NotBlank(message = "Vui lòng nhập tên hãng xe!")
    private String brandName;

    public static BrandMotorDTO parseBrandMotorDTO(BrandMotor brandMotor) {
        return new BrandMotorDTO(brandMotor.getBrandId(), brandMotor.getBrandName());
    }
}


