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

    @Column(name = "type_motor_name")
    private String typeMotorName;



    // One to many - Many to one
//    @OneToMany(mappedBy = "typeMotor")
//    private List<SeriesMotor> seriesMotorList;
//
//    @OneToMany(mappedBy = "typeMotor")
//    private List<DetailMotor> detailMotorList;
}
