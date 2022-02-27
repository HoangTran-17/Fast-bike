package com.codegym.service.dto;

import com.codegym.repository.model.ColorMotor;
import com.codegym.repository.model.DetailMotor;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

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

