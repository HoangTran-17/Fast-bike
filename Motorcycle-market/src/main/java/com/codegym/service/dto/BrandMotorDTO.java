package com.codegym.service.dto;

import com.codegym.repository.model.BrandMotor;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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


