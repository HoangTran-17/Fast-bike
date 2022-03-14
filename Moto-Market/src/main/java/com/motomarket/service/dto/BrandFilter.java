package com.motomarket.service.dto;

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
public class BrandFilter {
    private Long brandId;
    private String brandName;
    private String href;
    private boolean isSelected;

    public static BrandFilter parseBrandFilter(BrandMotor brandMotor, String href, Boolean bo) {
        BrandFilter brandFilter = new BrandFilter();
        brandFilter.setBrandId(brandMotor.getBrandId());
        brandFilter.setBrandName(brandMotor.getBrandName());
        brandFilter.setHref(href);
        brandFilter.setSelected(bo);

        return brandFilter;
    }
}
