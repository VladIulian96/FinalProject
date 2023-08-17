package com.iulian.FinalProject.service;

import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    public Product createProduct(Product product) {
        if(productExists(product.getId())) {
            throw new RuntimeException("User Already Exists!");
        }
        return productRepository.save(product);
    }

//    public void createProduct(String title, String description, int price, int stock, String uid) {
//        if(productExists(uid)) {
//            System.out.println("Product already exists!");
//            return;
//        }
//
//        Product product = new Product();
//        product.setTitle(title);
//        product.setDescription(description);
//        product.setPrice(price);
//        product.setStock(stock);
//        product.setUid(uid);
//
//        productRepository.save(product);
//    }

    public boolean productExists(int id) {
        return productRepository.findById(id) != null;
    }

    public boolean productExists(String uid) {
        return productRepository.findByuid(uid) != null;
    }


//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    public Product getProductByCode(String uid) {
//        return productRepository.findByuid(uid);
//    }
//
//    public Product saveProduct(Product product) {
//        return productRepository.save(product);
//    }
//
//    public void deleteProduct(String uid) {
//        productRepository.deleteByuid(uid);
//    }

}
