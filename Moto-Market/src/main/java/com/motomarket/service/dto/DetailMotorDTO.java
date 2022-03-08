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

    private BrandMotorDTO brandMotor;

    private SeriesMotorDTO seriesMotor;

    private TypeMotorDTO typeMotor;

    private ModelYearDTO modelYear;

    private ColorMotorDTO colorMotor;

    public static DetailMotorDTO parseDetailMotorDTO(DetailMotor detailMotor) {
        return new DetailMotorDTO(detailMotor.getDetailMotorId(),
                BrandMotorDTO.parseBrandMotorDTO(detailMotor.getBrandMotor()),
                SeriesMotorDTO.parseSeriesMotorDTO(detailMotor.getSeriesMotor()),
                TypeMotorDTO.parseTypeMotorDTO(detailMotor.getTypeMotor()),
                ModelYearDTO.parseModelYearDTO(detailMotor.getModelYear()),
                ColorMotorDTO.parseColorMotorDTO(detailMotor.getColorMotor()));
    }
}
