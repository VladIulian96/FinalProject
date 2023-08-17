package com.iulian.FinalProject.controller;

import com.iulian.FinalProject.model.Role;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import com.iulian.FinalProject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping(value="/adminPanel")
    public String admin_panel(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }
        return "adminPanel";
    }

    // Lists all users
    @GetMapping(value = "/adminPanel/users")
    public String users_panel(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }
        model.addAttribute("users", userRepository.findAll());
        return "usersPanel";
    }

    // Get user by id
    @GetMapping(value = "/adminPanel/user/{id}")
    public String edit_user_form(@PathVariable(name = "id") int userId, Model model) {
        model.addAttribute("user", userRepository.findById(userId));
        return "updateUserPage";
    }

    // Create a new user button
    @PostMapping(value = "/adminPanel/user/create_user")
    public String create_User(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if(!userService.userExists(request.getParameter("email"))) {
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));
            if (request.getParameter("role").equalsIgnoreCase("1")) {
                user.setRole(new Role(1, "USER"));
            } else if (request.getParameter("role").equalsIgnoreCase("2")) {
                user.setRole(new Role(2, "ADMIN"));
            }
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully created user: " + user.getUsername() + ".");
        } else {
            redirectAttributes.addFlashAttribute("message", "An user with this email already exists!");
        }
        return "redirect:/adminPanel/users";
    }

    // Called after submitting the update user form, saving the user details
    @PostMapping(value = "/adminPanel/user/save/{id}")
    public String save_user_form(@PathVariable(name = "id") int userId, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        User oldUser = userRepository.findById(userId);

        if(!userService.userExists(user.getEmail()) || oldUser.getEmail().equals(user.getEmail())) {
//        newUser.setId(user.getId());
            oldUser.setUsername(user.getUsername());
            oldUser.setEmail(user.getEmail());
//        newUser.setPassword(user.getPassword());

            if (user.getRole().getName().equalsIgnoreCase("1")) {
                oldUser.setRole(new Role(1, "USER"));
            } else if (user.getRole().getName().equalsIgnoreCase("2")) {
                oldUser.setRole(new Role(2, "ADMIN"));
            }
            userRepository.save(oldUser);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully updated user: " + oldUser.getUsername() + ".");
        } else {
            redirectAttributes.addFlashAttribute("message", "An user with this email already exists.");
        }
        return "redirect:/adminPanel/users";
    }

    // Delete user by id
    @GetMapping(value = "/adminPanel/user/delete_user")
    public String delete_user(@RequestParam(name = "id") int userId, RedirectAttributes redirectAttributes) {
        if(userService.userExists(userRepository.findById(userId).getEmail())) {
            redirectAttributes.addFlashAttribute("warningMessage", "Successfully deleted user: " + userRepository.findById(userId).getUsername() + ".");
            userRepository.deleteById(userId);
        } else {
            redirectAttributes.addFlashAttribute("message", "Cannot delete user. User does not exist.");
        }
        return "redirect:/adminPanel/users";
    }

}
