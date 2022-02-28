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
@Table(name = "model_year")
public class ModelYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_year_id", nullable = false)
    private Long modelYearId;

    @Column(name = "model_year_name")
    private int modelYearName;



// One to many - Many to one
    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private SeriesMotor seriesMotor;

    @OneToMany(mappedBy = "modelYear")
    private List<DetailMotor> detailMotorList;
}

