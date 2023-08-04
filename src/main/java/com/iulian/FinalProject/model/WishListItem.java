package com.iulian.FinalProject.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "wishlist")
public class WishListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}