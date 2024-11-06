package com.example.Project1.controller.OrderController;

import java.util.Locale.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.Project1.repository.CategoryRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.ProductRepository;

@Controller
public class AdminOrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
}
