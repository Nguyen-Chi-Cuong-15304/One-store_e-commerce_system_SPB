package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Product_Category;
import java.util.List;


@Repository
public interface Product_CategoryRepository extends JpaRepository<Product_Category, Integer> {

    @Query("SELECT p from Product_Category p WHERE p.productID = ?1 AND p.categoryID = ?2")
    Product_Category findByProductIDandCategoryID(int productID, int categoryID);

    @Query("SELECT p FROM Product_Category p WHERE p.productID = ?1")
    List<Product_Category> findByCategoryID(int categoryID);

    @Query("Select p from Product_Category p where p.productID = ?1")
    List<Product_Category> findByProductID(int productID);
}