package com.iulian.FinalProject.service;

import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.model.WishListItem;
import com.iulian.FinalProject.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    @Autowired
    WishListRepository wishListRepository;

    public void addToWishList(User user, Product product) {

        if(!itemExists(user.getId(), product.getId())) {
            WishListItem wishListItem = new WishListItem();
            wishListItem.setUser(user);
            wishListItem.setProduct(product);
            wishListRepository.save(wishListItem);
        } else {
            throw new RuntimeException("Cannot add to wishlist!");
        }
    }

    public boolean itemExists(int userId, int productId) {
        return wishListRepository.findByUser_IdAndProduct_Id(userId, productId) != null;
    }

    public void removeFromWishList(User user, Product product) {
        WishListItem wishListItem = wishListRepository.findByUserAndProduct(user, product);

        if(wishListItem != null) {
            wishListRepository.delete(wishListItem);
        } else {
            throw new RuntimeException("Item does not exist.");
        }
    }

}
