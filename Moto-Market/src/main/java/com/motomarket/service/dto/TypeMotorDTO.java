package com.motomarket.service.dto;

import com.motomarket.repository.model.TypeMotor;
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
public class TypeMotorDTO {

    private Long typeMotorId;

    @NotBlank(message = "Vui lòng nhập tên loại xe!")
    private String typeMotorName;

    TypeMotorDTO parseTypeMotorDTO(TypeMotor typeMotor) {
        return new TypeMotorDTO(typeMotor.getTypeMotorId(), typeMotor.getTypeMotorName());
    }

    public TypeMotor parseTypeMotor(TypeMotorDTO typeMotorDTO) {
        return new TypeMotor(typeMotorDTO.getTypeMotorId(), typeMotorDTO.getTypeMotorName());
    }
}
