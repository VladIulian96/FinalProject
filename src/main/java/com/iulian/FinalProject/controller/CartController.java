package com.iulian.FinalProject.controller;

import com.iulian.FinalProject.model.CartItem;
import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.CartRepository;
import com.iulian.FinalProject.repository.ProductRepository;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import com.iulian.FinalProject.service.CartService;
import com.iulian.FinalProject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CartController {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartService cartService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @GetMapping(value = "/cart")
    public String show_cart(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        User user = null;
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }

        if(user != null) {
            model.addAttribute("cartItems", cartRepository.findByUserId(user.getId()));

            int totalPrice = 0;
            for(CartItem cartItem : cartRepository.findByUserId(user.getId())) {
                totalPrice += cartItem.getProduct().getPrice();
            }
            model.addAttribute("totalPrice", totalPrice);

        }
        return "cart";
    }

    @GetMapping(value = "/cart/add")
    public String addItemToCart(@RequestParam(name = "id") int productId, @AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, RedirectAttributes redirectAttributes) {
        if(userDetailsPrincipal != null) {
            if(productService.productExists(productId)) {
                User user = userRepository.findByEmail(userDetailsPrincipal.getEmail());
                Product product = productRepository.findById(productId);

                if(!cartService.itemExists(user.getId(), product.getId())) {
                    cartService.addToCart(user, product);
                } else {
                    redirectAttributes.addFlashAttribute("message", "Product is already in the cart!");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "Product does not exist!");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not logged in");
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/cart/remove")
    public String removeItemFromCart(@RequestParam(name = "id") int productId, @AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, RedirectAttributes redirectAttributes) throws IOException {
        if(userDetailsPrincipal != null) {
            if (productService.productExists(productId)) {
                User user = userRepository.findByEmail(userDetailsPrincipal.getEmail());
                Product product = productRepository.findById(productId);
                if(cartService.itemExists(user.getId(), product.getId())) {
                    redirectAttributes.addFlashAttribute("message", "Removed item from cart");
                    cartService.removeFromCart(user, product);
                } else {
                    redirectAttributes.addFlashAttribute("message", "Item does not exist!");
                }
            }
        }

        return "redirect:/cart";
    }

}
