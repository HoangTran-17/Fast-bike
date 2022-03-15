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
    private String param;
    private String name;
    private String href;
    private boolean isSelected;

    public CapacityFilter(String param,String name) {
        this.param = param;
        this.name = name;
    }
}
