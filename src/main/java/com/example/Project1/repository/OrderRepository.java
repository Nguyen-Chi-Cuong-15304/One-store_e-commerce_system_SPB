package com.example.Project1.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Orders;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    public Orders findByOrderID(int orderID);
    public Orders findByOrderDate(Date orderDate);
    public List<Orders> findByUserID(int userID);
    @Query("SELECT o FROM Orders o WHERE MONTH(o.orderDate) = ?1 AND YEAR(o.orderDate) = ?2")
    public List<Orders> findByMonthAndYear(int month, int year);

    @Query("SELECT COUNT(o) FROM Orders o WHERE YEAR(o.orderDate) = ?1")
    int countTotalOrdersForYear(int currentYear);

    @Query("SELECT SUM(o.totalAmount) FROM Orders o WHERE YEAR(o.orderDate) = ?1")
    Double calculateTotalRevenueForYear(int currentYear);

    @Query("SELECT o FROM Orders o ORDER BY o.orderDate DESC LIMIT 5")
    List<Orders> getRecentOrders();
}
