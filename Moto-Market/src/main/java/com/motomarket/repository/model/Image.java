package com.motomarket.repository.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "image")
@ToString
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
