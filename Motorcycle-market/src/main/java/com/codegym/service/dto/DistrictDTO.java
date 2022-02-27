package com.codegym.service.dto;


import com.codegym.repository.model.District;
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
public class DistrictDTO {

    private Long districtId;

    @NotBlank(message = "Vui lòng nhập tên huyện!")
    private String districtName;

    public static DistrictDTO parseDistrictDTO(District district){
        return new DistrictDTO(district.getDistrictId(), district.getDistrictName());
    }

}

