package com.example.Project1.controller.product_customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Project1.entity.Category;
import com.example.Project1.entity.Product;
import com.example.Project1.entity.Region;
import com.example.Project1.repository.CategoryRepository;
import com.example.Project1.repository.ProductRepository;
import com.example.Project1.repository.RegionRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/navi")
public class pageController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RegionRepository regionRepository;
    
    @GetMapping("/categoryDefault")
    public String categoryDefault(Model model, @RequestParam(value="sortOption", required = false) String sortOption) {
        List<Product> products = null;
        if(sortOption == null) {
            products = productRepository.findByOrderByProductIDDesc();
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByOrderByPriceAsc();
                    break;
                case "priceDesc":
                    products = productRepository.findByOrderByPriceDesc();
                    break;
                case "viewCountDesc":
                    products = productRepository.findByOrderByViewCountDesc();
                    break;
                default:
                    products = productRepository.findByOrderByProductIDDesc();
                    break;
            }
        }
        model.addAttribute("products", products);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "category";
    }
    @GetMapping("/showChoosenCategory/{id}")
    public String showChoosenCategory(@PathVariable int id, @RequestParam(value="sortOption", required = false) String sortOption ,Model model, HttpServletRequest request) {
        List<Product> products = null;
        if(sortOption == null) {
            products = productRepository.findByCategoryID(id);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByCategoryIDOrderByPriceAsc(id);
                    break;
                case "priceDesc":
                    products = productRepository.findByCategoryIDOrderByPriceDesc(id);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByCategoryIDOrderByViewCountDesc(id);
                    break;
                default:
                    products = productRepository.findByCategoryIDOrderByProductIDDesc(id);
                    break;
            }
        }
        model.addAttribute("products", products);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("currentID", id);
        return "category_id";
    }
    
    @GetMapping("/categoryByRegion")
    public String categoryByRegion(Model model, @RequestParam(value="sortOption", required = false) String sortOption) {
        List<Region> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        List<Product> products = null;
        if(sortOption == null) {
            products = productRepository.findByOrderByProductIDDesc();
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByOrderByPriceAsc();
                    break;
                case "priceDesc":
                    products = productRepository.findByOrderByPriceDesc();
                    break;
                case "viewCountDesc":
                    products = productRepository.findByOrderByViewCountDesc();
                    break;
                default:
                    products = productRepository.findByOrderByProductIDDesc();
                    break;
            }
        }
        model.addAttribute("products", products);

        return "product_region";
    }

    @GetMapping("/categoryByRegion/{id}")
    public String findByRegionID(@PathVariable int id, @RequestParam(value="sortOption", required = false) String sortOption, Model model, HttpServletRequest request) {
        List<Product> products = null;
        if(sortOption == null) {
            products = productRepository.findByRegionID(id);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByRegionIDOrderByPriceAsc(id);
                    break;
                case "priceDesc":
                    products = productRepository.findByRegionIDOrderByPriceDesc(id);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByRegionIDOrderByViewCountDesc(id);
                    break;
                default:
                    products = productRepository.findByRegionIDOrderByProductIDDesc(id);
                    break;
            }
        }
        model.addAttribute("products", products);
        List<Region> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("currentID", id);
        return "region_id";
    }
    
    
}
