package com.motomarket.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "brand_motor")
public class BrandMotor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Column(name = "brand_name")
    private String brandName;

    // One to many - Many to one
    @OneToMany(mappedBy = "brandMotor")
    private List<SeriesMotor> seriesMotorList;

    @OneToMany(mappedBy = "brandMotor")
    private List<DetailMotor> detailMotorList;

    public BrandMotor(Long brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public BrandMotor(String brandName) {
        this.brandName = brandName;
    }
}
