package com.motomarket.service.dto;


import com.motomarket.repository.model.District;
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
public class DistrictDTO {

    private Long districtId;

    @NotBlank(message = "Vui lòng nhập tên huyện!")
    private String districtName;

    public static DistrictDTO parseDistrictDTO(District district){
        return new DistrictDTO(district.getDistrictId(), district.getDistrictName());
    }

}

