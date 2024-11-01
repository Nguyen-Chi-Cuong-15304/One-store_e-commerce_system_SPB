package com.example.Project1.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    public Order findByOrderID(int orderID);
    public Order findByOrderDate(Date orderDate);
}
