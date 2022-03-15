package com.motomarket.service.filter;

import com.motomarket.repository.model.TypeMotor;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class TypeMotorFilter {
    private Long typeMotorId;
    private String typeMotorName;
    private String href;
    private boolean isSelected;

    public static TypeMotorFilter parseTypeMotorFilter(TypeMotor typeMotor, String href, Boolean bo) {
        TypeMotorFilter typeMotorFilter = new TypeMotorFilter();
        typeMotorFilter.setTypeMotorId(typeMotor.getTypeMotorId());
        typeMotorFilter.setTypeMotorName(typeMotor.getTypeMotorName());
        typeMotorFilter.setHref(href);
        typeMotorFilter.setSelected(bo);

        return typeMotorFilter;
    }
}
