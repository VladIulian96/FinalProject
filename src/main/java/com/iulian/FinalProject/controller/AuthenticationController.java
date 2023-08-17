package com.iulian.FinalProject.controller;

import com.iulian.FinalProject.model.Role;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.ProductRepository;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import com.iulian.FinalProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// @RestController is used for web pages that return Json or XML data.
@Controller
@RequiredArgsConstructor

public class AuthenticationController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping(value="/home")
    public String home(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("products", productRepository.findAll());

        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }

        return "homePage";
    }

    @GetMapping(value="/account")
    public String account_information(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }
        return "accountPage";
    }

    @GetMapping(value="/about")
    public String about(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }
        return "aboutPage";
    }

    @GetMapping(value="/register")
    public String register() {
        return "registerPage";
    }

    @PostMapping(value="/register")
    public String register(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        if(!userService.userExists(user.getEmail())) {
            user.setRole(new Role(1, "USER"));
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("loginPageMessage", "Account successfully created!");
            return "redirect:/login";
        } else {
            model.addAttribute("message", "An user with this email already exists!");
        }
        return "registerPage";
    }

    @GetMapping(value="/login")
    public String showLoginForm() {
        return "loginPage";
    }

    @PostMapping(value="/login")
    public String submitForm() {
        return "loginPage";
    }



}
