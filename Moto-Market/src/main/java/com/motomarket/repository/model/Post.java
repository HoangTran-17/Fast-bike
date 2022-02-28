package com.motomarket.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "status_post")
    private StatusPost statusPost;

    private String title;

    @Column(name = "model_motor")
    private String modelMotor;

    @Column(name = "kilometer_count")
    private String kilometerCount;

    private String description;

    private Double price;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_phone_number")
    private String sellerPhoneNumber;

    @Column(name = "post_date")
    private Date postDate;

    private Ownership ownership;


// One to many - Many to one
    @OneToMany(mappedBy = "posts")
    private List<Image> imageList;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "detail_motor_id", nullable = false)
    private DetailMotor detailMotor;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    public Post( StatusPost statusPost, String title, String modelMotor, String kilometerCount, String description, Double price, String sellerName, String sellerPhoneNumber, Date postDate, Ownership ownership, User user, DetailMotor detailMotor, District district) {
        this.statusPost = statusPost;
        this.title = title;
        this.modelMotor = modelMotor;
        this.kilometerCount = kilometerCount;
        this.description = description;
        this.price = price;
        this.sellerName = sellerName;
        this.sellerPhoneNumber = sellerPhoneNumber;
        this.postDate = postDate;
        this.ownership = ownership;
        this.user = user;
        this.detailMotor = detailMotor;
        this.district = district;
    }
}


