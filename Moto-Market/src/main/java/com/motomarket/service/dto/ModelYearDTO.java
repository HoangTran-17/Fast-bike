package com.motomarket.service.dto;

import com.motomarket.repository.model.ModelYear;
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
public class ModelYearDTO {

    private Long modelYearId;

    @NotBlank(message = "Vui lòng nhập đời xe!")
    private int modelYearName;

    private SeriesMotorDTO seriesMotorDTO;

    public static ModelYearDTO parseModelYearDTO(ModelYear modelYear){

        return new ModelYearDTO(modelYear.getModelYearId(), modelYear.getModelYearName(),SeriesMotorDTO.parseSeriesMotorDTO(modelYear.getSeriesMotor()));
    }
}

