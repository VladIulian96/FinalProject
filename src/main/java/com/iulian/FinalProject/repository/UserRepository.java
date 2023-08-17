package com.iulian.FinalProject.repository;

import com.iulian.FinalProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User deleteByEmail(String email);

    User findById(int id);
    User deleteById(int id);

}
