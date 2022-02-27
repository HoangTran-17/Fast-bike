package com.codegym.service.dto;

import com.codegym.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString

public class DetailMotorDTO {

    private Long detailMotorId;

    public static DetailMotorDTO parseDetailMotorDTO(DetailMotor detailMotor) {
        return new DetailMotorDTO(detailMotor.getDetailMotorId());
    }

}
