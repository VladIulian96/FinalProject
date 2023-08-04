package com.iulian.FinalProject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private int price;
    private int stock;
    private String uid;
    private String image;

    @Transient
    public String getPhotosImagePath() {
        if (image == null) return null;

//        return "/images/productImages/" + id +"/" + uid +".png";
        return "/images/productImages/" + id +"/" + image;
    }

}