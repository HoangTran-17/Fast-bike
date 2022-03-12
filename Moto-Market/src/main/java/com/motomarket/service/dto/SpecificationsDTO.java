package com.motomarket.service.dto;

import com.motomarket.repository.model.SeriesMotor;
import com.motomarket.repository.model.Specifications;
import lombok.*;
import lombok.experimental.Accessors;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class SpecificationsDTO {

    private Long specifications_id;

    private String engine;

    private String bootSystem;

    private String compressionRatio;

    private String coolingSystem;

    private String capacity;

    private String gearBox;

    private String piston;

    private String maxWattage;

    private String maxTorque;

    private String size;

    private String lengthWheelToWheel;

    private String saddleHeight;

    private String underside;

    private String petrolTank;

    private String weight;

    private String brakes;

    private String frontTire;

    private String rearTire;

    public static SpecificationsDTO parseSpecificationsDTO(Specifications specifications) {
        return new SpecificationsDTO(specifications.getSpecifications_id(),
                specifications.getEngine(), specifications.getBootSystem(),
                specifications.getCompressionRatio(), specifications.getCoolingSystem(),
                specifications.getCapacity(), specifications.getGearBox(),
                specifications.getPiston(), specifications.getMaxWattage(), specifications.getMaxTorque(),
                specifications.getSize(), specifications.getLengthWheelToWheel(),
                specifications.getSaddleHeight(), specifications.getUnderside(),
                specifications.getPetrolTank(), specifications.getWeight(), specifications.getBrakes(),
                specifications.getFrontTire(), specifications.getRearTire());
    }

    public static Specifications parseSpecifications(SpecificationsDTO specificationsDTO) {
        return new Specifications(specificationsDTO.getSpecifications_id(), specificationsDTO.getEngine(),
                specificationsDTO.getBootSystem(), specificationsDTO.getCompressionRatio(),
                specificationsDTO.getCoolingSystem(), specificationsDTO.getCapacity(),
                specificationsDTO.getGearBox(), specificationsDTO.getPiston(), specificationsDTO.getMaxWattage(),
                specificationsDTO.getMaxTorque(), specificationsDTO.getSize(), specificationsDTO.getLengthWheelToWheel(),
                specificationsDTO.getSaddleHeight(), specificationsDTO.getUnderside(), specificationsDTO.getPetrolTank(),
                specificationsDTO.getWeight(), specificationsDTO.getBrakes(), specificationsDTO.getFrontTire(),
                specificationsDTO.getRearTire(),new SeriesMotor());
    }



}

