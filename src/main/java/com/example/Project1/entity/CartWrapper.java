package com.example.Project1.entity;

import java.util.List;

public class CartWrapper {
     List<CartItem> cartItems;
     Order order;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    public void addItems(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }
}
