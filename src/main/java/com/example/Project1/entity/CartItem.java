package com.example.Project1.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartitem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemID;

    private int cartID;
    private int productID;
    private int quantity;
    private String productName;
    private BigDecimal price;
    private String linkImg;

    public int getCartItemID() {
        return cartItemID;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal bigDecimal) {
        this.price = bigDecimal;
    }
    public String getLinkImg() {
        return linkImg;
    }
    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
    public void setCartItemID(int cartItemID) {
        this.cartItemID = cartItemID;
    }
    public int getCartID() {
        return cartID;
    }
    public void setCartID(int cartID) {
        this.cartID = cartID;
    }
    public int getProductID() {
        return productID;
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
    
    
}
