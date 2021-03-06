package com.motomarket.service.dto;

import com.motomarket.repository.model.*;
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
public class SeriesMotorDTO {

    private Long seriesId;

    @NotBlank(message = "Vui lòng nhập tên dòng xe!")
    private String seriesName;

    @NotBlank(message = "Vui lòng nhập phân khối xe!")
    private int capacity;

    private Long brandId;

    private TypeMotorDTO typeMotorDTO;

    private SpecificationsDTO specificationsDTO;

    public static SeriesMotorDTO parseSeriesMotorDTO(SeriesMotor seriesMotor) {
        return new SeriesMotorDTO(seriesMotor.getSeriesId(), seriesMotor.getSeriesName(),
                seriesMotor.getCapacity(), seriesMotor.getBrandMotor().getBrandId(),
                new TypeMotorDTO().parseTypeMotorDTO(seriesMotor.getTypeMotor()),
                new SpecificationsDTO().parseSpecificationsDTO(seriesMotor.getSpecifications()));
    }


}


