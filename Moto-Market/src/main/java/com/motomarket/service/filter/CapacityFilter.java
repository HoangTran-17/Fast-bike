package com.motomarket.service.filter;

import com.motomarket.repository.model.BrandMotor;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class CapacityFilter {
    private Integer min;
    private Integer max;
    private String param;
    private String name;
    private String href;
    private boolean isSelected;

    public CapacityFilter(Integer min,Integer max,String param,String name) {
        this.min = min;
        this.max = max;
        this.param = param;
        this.name = name;
    }
}
