package com.example.Project1.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Project1.entity.Product;
import com.example.Project1.entity.Supplier;
import com.example.Project1.repository.ProductRepository;
import com.example.Project1.repository.SupplierRepository;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("message", "Welcome to Thymeleaf with Spring Boot!"); 
        List<Supplier> suppliers = supplierRepository.findTop3Suppliers(PageRequest.of(0, 4));
        model.addAttribute("suppliers", suppliers);
        List<Product> products = productRepository.findTopViewProducts(PageRequest.of(0, 4));
        model.addAttribute("products", products);
        return "index";  // Trả về tên file template (index.html)
    }
    @GetMapping("/contact")
    public String contact() {
        return  "contact";
    }

    @GetMapping("/about")
    public String about() {
        return  "about";
    }
    
    
}