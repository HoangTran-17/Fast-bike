package com.motomarket.repository.model;

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
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

   @Column(name = "image_name")
   private String imageName;


// One to many - Many to one
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post posts;

}
