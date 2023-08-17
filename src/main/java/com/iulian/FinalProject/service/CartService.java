package com.iulian.FinalProject.service;

import com.iulian.FinalProject.model.CartItem;
import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public void addToCart(User user, Product product) {

        if(!itemExists(user.getId(), product.getId())) {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartRepository.save(cartItem);
        } else {
            throw new RuntimeException("Cannot add to cart!");
        }
    }

    public boolean itemExists(int userId, int productId) {
        return cartRepository.findByUser_IdAndProduct_Id(userId, productId) != null;
    }

    public void removeFromCart(User user, Product product) {
        CartItem cartItem = cartRepository.findByUserAndProduct(user, product);

        if(cartItem != null) {
            cartRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Item does not exist.");
        }
    }

}
