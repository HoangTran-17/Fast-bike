package com.codegym.model;

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
@Table
public class TypeMotor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_motor_id", nullable = false)
    private Long typeMotorId;

    @OneToMany(mappedBy = "typeMotor")
    private List<SeriesMotor> seriesMotorList;

    @Column(name = "type_motor_name")
    private String typeMotorName;

    @OneToMany(mappedBy = "typeMotor")
    private List<DetailMotor> detailMotorList;
}
