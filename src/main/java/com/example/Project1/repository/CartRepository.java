package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserID(int userID);
    
}
