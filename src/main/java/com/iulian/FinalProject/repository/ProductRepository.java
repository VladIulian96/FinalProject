package com.iulian.FinalProject.repository;

import com.iulian.FinalProject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findById(int id);
    Product deleteById(int id);

    Product findByuid(String uid);
    Product deleteByuid(String uid);

}
