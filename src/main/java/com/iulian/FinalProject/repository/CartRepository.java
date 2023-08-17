package com.iulian.FinalProject.repository;

import com.iulian.FinalProject.model.CartItem;
import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {

    @Query("SELECT wl FROM CartItem wl WHERE wl.user.id = ?1 AND wl.product.id = ?2")
    CartItem findByUser_IdAndProduct_Id(int userId, int productId);
    List<CartItem> findByUserId(int userId);
    CartItem findByUserAndProduct(User user, Product product);

}
