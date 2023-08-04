package com.iulian.FinalProject.security;

import com.iulian.FinalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    public UserService userService;

    @Bean
    public PasswordEncoder bcryptpasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(bcryptpasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/images/**", "/js/**", "/videos/**").permitAll();
                    auth.requestMatchers("/login", "/register", "/home", "/about", "/product").permitAll();
                    auth.requestMatchers("/cart/**", "/wishlist/**", "/account").hasAnyAuthority("USER", "ADMIN");
                    //auth.requestMatchers("/delete_user").hasAuthority("ADMIN");
                    auth.requestMatchers("/adminPanel/**").hasAuthority("ADMIN");
                })

                .formLogin(form -> {
                    form.loginPage("/login");
                    form.loginProcessingUrl("/login");
                    form.usernameParameter("email");
                    form.defaultSuccessUrl("/home");
                })

                .logout(logout -> {
                    //logout.logoutUrl("/logout");
//                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessUrl("/home");
                })

                .csrf(xd -> xd.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }


//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService());
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }


}
