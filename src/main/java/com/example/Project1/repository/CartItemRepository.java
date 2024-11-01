package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartID(int cartID);

    @Query("SELECT c FROM CartItem c WHERE c.productID = ?1 AND c.cartID = ?2")
    CartItem findByProductIDandCartID(int productID, int cartID);

    @Query("SELECT c FROM CartItem c WHERE c.cartID = ?1")
    List<CartItem> findByCartIDList(int cartID);

    CartItem findByCartItemID(int cartItemID);
}
