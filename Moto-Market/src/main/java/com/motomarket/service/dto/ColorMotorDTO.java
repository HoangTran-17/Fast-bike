package com.motomarket.service.dto;

import com.motomarket.repository.model.ColorMotor;
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
public class ColorMotorDTO {
    private Long colorId;

    @NotBlank(message = "Vui lòng nhập màu sắc!")
    private String colorName;

    public static ColorMotorDTO parseColorMotorDTO(ColorMotor colorMotor) {
        return new ColorMotorDTO(colorMotor.getColorId(), colorMotor.getColorName());
    }


}

