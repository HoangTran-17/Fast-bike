package com.codegym.repository.model;

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
@Table(name = "color_motor")
public class ColorMotor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id", nullable = false)
    private Long colorId;

    @Column(name = "color_name")
    private String colorName;

// One to many - Many to one
    @OneToMany(mappedBy = "colorMotor")
    private List<DetailMotor> detailMotorList;
}

