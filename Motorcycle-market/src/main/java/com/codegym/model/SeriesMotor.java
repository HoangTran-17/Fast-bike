package com.codegym.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "series_motor")
public class SeriesMotor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id", nullable = false)
    private Long seriesId;

     @OneToOne(mappedBy = "seriesMotor")
     private Specifications specifications;

    @Column(name = "series_name")
    @NotBlank(message = "series name can't blank!")
    private String seriesName;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandMotor brandMotor;

     @ManyToOne
     @JoinColumn(name = "typeMotor", nullable = false)
     private TypeMotor typeMotor;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "seriesMotor")
    private List<ModelYear> modelYearList;

    @OneToMany(mappedBy = "seriesMotor")
    private List<DetailMotor> detailMotorList;

}


