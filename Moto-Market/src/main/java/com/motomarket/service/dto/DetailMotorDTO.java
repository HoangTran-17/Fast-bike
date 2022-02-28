package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
import lombok.*;
import lombok.experimental.Accessors;

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
