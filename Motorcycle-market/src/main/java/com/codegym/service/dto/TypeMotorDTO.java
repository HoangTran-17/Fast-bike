package com.codegym.service.dto;

import com.codegym.repository.model.DetailMotor;
import com.codegym.repository.model.SeriesMotor;
import com.codegym.repository.model.TypeMotor;
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
