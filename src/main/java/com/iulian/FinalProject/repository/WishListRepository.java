package com.iulian.FinalProject.repository;

import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishListItem, Integer> {

    @Query("SELECT wl FROM WishListItem wl WHERE wl.user.id = ?1 AND wl.product.id = ?2")
    WishListItem findByUser_IdAndProduct_Id(int userId, int productId);
    List<WishListItem> findByUserId(int userId);
    WishListItem findByUserAndProduct(User user, Product product);
}
