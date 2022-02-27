package com.codegym.service.dto;

import com.codegym.repository.model.DetailMotor;
import com.codegym.repository.model.ModelYear;
import com.codegym.repository.model.SeriesMotor;
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
public class ModelYearDTO {

    private Long modelYearId;

    @NotBlank(message = "Vui lòng nhập đời xe!")
    private int modelYearName;

    private SeriesMotorDTO seriesMotorDTO;

    public static ModelYearDTO parseModelYearDTO(ModelYear modelYear){

        return new ModelYearDTO(modelYear.getModelYearId(), modelYear.getModelYearName(),SeriesMotorDTO.parseSeriesMotorDTO(modelYear.getSeriesMotor()));
    }
}

