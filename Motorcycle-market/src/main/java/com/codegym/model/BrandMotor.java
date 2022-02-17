package com.codegym.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "brand_motor")
public class BrandMotor {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @NotBlank(message = "tên hãng không được để trống!")
    @Column(name = "brand_name")
    private String brandName;

    @OneToMany(mappedBy = "brandMotor")
    private List<SeriesMotor> seriesMotorList;

    @OneToMany(mappedBy = "brandMotor")
    private List<DetailMotor> detailMotorList;
}
