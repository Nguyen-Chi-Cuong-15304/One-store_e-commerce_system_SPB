package com.example.Project1.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderitem")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemID;

    private int orderID;
    private int productID;
    private int quantity;
    private String productName;
    private BigDecimal price;
    private String linkImg;

    public int getOrderItemID() {
        return orderItemID;
    }
    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }
    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public int getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getLinkImg() {
        return linkImg;
    }
    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
   
    
    

}
