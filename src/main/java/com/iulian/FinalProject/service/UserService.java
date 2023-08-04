package com.iulian.FinalProject.service;

import com.iulian.FinalProject.model.Role;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException("Email not found!");
        }
        return new UserDetailsPrincipal(user);
    }

    public User createUser(User user) {
        if(userExists(user.getEmail())) {
            throw new RuntimeException("User Already Exists!");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User createUser(String username, String email, String password, int roleId) {
        if(userExists(email)) {
            throw new RuntimeException("User Already Exists!");
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        if(roleId == 1) {
            user.setRole(new Role(1, "USER"));
        } else if (roleId == 2) {
            user.setRole(new Role(2, "ADMIN"));
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User deleteUser(User user) {
        if(!userExists(user.getEmail())) {
            throw new RuntimeException("User doesn't exist!");
        }
        return userRepository.deleteByEmail(user.getEmail());
    }

    public User deleteUser(String email) {
        if(!userExists(email)) {
            throw new RuntimeException("User doesn't exist!");
        }
        return userRepository.deleteByEmail(email);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean isPasswordCorrect(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            return false;
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        return encryptedPassword.equals(userRepository.findByEmail(email).getPassword());
    }

}
