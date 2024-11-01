package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product findByproductName(String productName);
    
    @Query("SELECT p FROM Product p Order by p.viewCount DESC")
    List<Product> findTopViewProducts(PageRequest pageRequest);

    public Product findByProductID(int productID);
}