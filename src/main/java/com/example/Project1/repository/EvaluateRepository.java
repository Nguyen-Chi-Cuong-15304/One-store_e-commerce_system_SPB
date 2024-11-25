package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Evaluate_Product;

@Repository
public interface EvaluateRepository extends JpaRepository<Evaluate_Product, Integer> {
    @Query("SELECT e FROM Evaluate_Product e WHERE e.productID = :productID")
    List<Evaluate_Product> findByProductID(int productID);
}
