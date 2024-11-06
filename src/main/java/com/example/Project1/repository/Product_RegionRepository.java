package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Product_Region;
import java.util.List;


@Repository
public interface Product_RegionRepository extends JpaRepository<Product_Region, Integer> {
    @Query("select p from Product_Region p where p.productID= ?1")
    List<Product_Region> findByProductID(int productID);
}
