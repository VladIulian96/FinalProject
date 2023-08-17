package com.iulian.FinalProject.controller;

import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.ProductRepository;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.repository.WishListRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import com.iulian.FinalProject.service.ProductService;
import com.iulian.FinalProject.service.WishListService;
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
public class WishListController {

    @Autowired
    WishListRepository wishListRepository;
    @Autowired
    WishListService wishListService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;

    @GetMapping(value = "/wishlist")
    public String show_wishlist(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        User user = null;
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }

//        model.addAttribute("wishlistItems", wishListRepository.findAll());
        if(user != null) {
            model.addAttribute("wishlistItems", wishListRepository.findByUserId(user.getId()));
        }

        return "wishlist";
    }

    @GetMapping(value = "/wishlist/add")
    public String addItemToWishList(@RequestParam(name = "id") int productId, @AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, RedirectAttributes redirectAttributes) {
        if(userDetailsPrincipal != null) {
            if(productService.productExists(productId)) {
                User user = userRepository.findByEmail(userDetailsPrincipal.getEmail());
                Product product = productRepository.findById(productId);

//                if(!wishListService.itemExists(user.getId(), product.getId())) {
//                    wishListService.addToWishList(user, product);
//                } else {
//                    redirectAttributes.addFlashAttribute("message", "Item already exists!");
//                }
                if(!wishListService.itemExists(user.getId(), product.getId())) {
                    wishListService.addToWishList(user, product);
                } else {
                    redirectAttributes.addFlashAttribute("message", "Product is already in the wishlist!");
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "Product does not exist!");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not logged in");
        }
        return "redirect:/home";
    }

    @GetMapping(value = "/wishlist/remove")
    public String removeItemFromWishList(@RequestParam(name = "id") int productId, @AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, RedirectAttributes redirectAttributes) throws IOException {
        if(userDetailsPrincipal != null) {
            if (productService.productExists(productId)) {
                User user = userRepository.findByEmail(userDetailsPrincipal.getEmail());
                Product product = productRepository.findById(productId);
                if(wishListService.itemExists(user.getId(), product.getId())) {
                    wishListService.removeFromWishList(user, product);
                } else {
                    redirectAttributes.addFlashAttribute("message", "Item does not exist!");
                }
            }
        }

        return "redirect:/wishlist";
    }

}
