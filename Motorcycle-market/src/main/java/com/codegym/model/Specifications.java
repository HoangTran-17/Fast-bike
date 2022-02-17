package com.codegym.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specifications")
public class Specifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specifications_id", nullable = false)
    private Long specifications_id;


     @OneToOne(cascade = CascadeType.ALL)
     @JoinColumn(name = "series_motor", referencedColumnName = "series_id")
     private SeriesMotor seriesMotor;

    @Column(name = "engine")
    private String engine;

    @Column(name = "boot_system")
    private String bootSystem;

    @Column(name = "compression_ratio")
    private String compressionRatio;

    @Column(name = "cooling_system")
    private String coolingSystem;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "gear_box")
    private String gearBox;

    @Column(name = "piston")
    private String piston;

    @Column(name = "max_wattage")
    private String maxWattage;

    @Column(name = "max_torque")
    private String maxTorque;

    @Column(name = "size")
    private String size;

    @Column(name = "length_wheel_to_wheel")
    private String lengthWheelToWheel;

    @Column(name = "saddle_height")
    private String saddleHeight;

    @Column(name = "underside")
    private String underside;

    @Column(name = "petrol_tank")
    private String petrolTank;

    @Column(name = "weight")
    private String weight;

    @Column(name = "brakes")
    private String brakes;

    @Column(name = "front_tire")
    private String frontTire;

    @Column(name = "rear_tire")
    private String rearTire;

}

