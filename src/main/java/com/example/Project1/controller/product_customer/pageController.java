package com.example.Project1.controller.product_customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String categoryDefault(Model model, @RequestParam(required = false) String sortOption,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        
        Page<Product> products = null;
        Pageable pageable = PageRequest.of(page, size);
        if(sortOption == null) {
            products = productRepository.findByOrderByProductIDDesc(pageable);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByOrderByPriceAsc( pageable);
                    break;
                case "priceDesc":
                    products = productRepository.findByOrderByPriceDesc( pageable);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByOrderByViewCountDesc( pageable);
                    break;
                default:
                    products = productRepository.findByOrderByProductIDDesc( pageable);
                    break;
            }
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getNumber());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("sortOption", sortOption);
        return "category";
    }
    @GetMapping("/showChoosenCategory/{id}")
    public String showChoosenCategory(@PathVariable int id, @RequestParam(required = false) String sortOption ,
        Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "6") int size) {

        Page<Product> products = null;
        Pageable pageable = PageRequest.of(page, size);
        //List<Product> products = null;
        if(sortOption == null) {
            products = productRepository.findByCategoryID(id, pageable);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByCategoryIDOrderByPriceAsc(id, pageable);
                    break;
                case "priceDesc":
                    products = productRepository.findByCategoryIDOrderByPriceDesc(id , pageable);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByCategoryIDOrderByViewCountDesc(id, pageable);
                    break;
                default:
                    products = productRepository.findByCategoryIDOrderByProductIDDesc(id, pageable);
                    break;
            }
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getNumber());
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("currentID", id);
        model.addAttribute("sortOption", sortOption);
        return "category_id";
    }
    
    @GetMapping("/categoryByRegion")
    public String categoryByRegion(Model model, @RequestParam(required = false) String sortOption,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        
        List<Region> regions = regionRepository.findAll();
        Page<Product> products = null;
        Pageable pageable = PageRequest.of(page, size);
        model.addAttribute("regions", regions);
        
        if(sortOption == null) {
            products = productRepository.findByOrderByProductIDDesc( pageable);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByOrderByPriceAsc(pageable);
                    break;
                case "priceDesc":
                    products = productRepository.findByOrderByPriceDesc(pageable);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByOrderByViewCountDesc(pageable);
                    break;
                default:
                    products = productRepository.findByOrderByProductIDDesc(pageable);
                    break;
            }
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("sortOption", sortOption);   
        return "product_region";
    }

    @GetMapping("/categoryByRegion/{id}")
    public String findByRegionID(@PathVariable int id, @RequestParam(required = false) String sortOption, 
        Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        
        Page<Product> products = null;
        Pageable pageable = PageRequest.of(page, size);
        if(sortOption == null) {
            products = productRepository.findByRegionID(id, pageable);
        } else {
            switch(sortOption) {
                case "priceAsc":
                    products = productRepository.findByRegionIDOrderByPriceAsc(id, pageable);
                    break;
                case "priceDesc":
                    products = productRepository.findByRegionIDOrderByPriceDesc(id, pageable);
                    break;
                case "viewCountDesc":
                    products = productRepository.findByRegionIDOrderByViewCountDesc(id, pageable);
                    break;
                default:
                    products = productRepository.findByRegionIDOrderByProductIDDesc(id, pageable);
                    break;
            }
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", products.getNumber());
        List<Region> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("currentURI", request.getRequestURI());
        model.addAttribute("currentID", id);
        model.addAttribute("sortOption", sortOption);
        return "region_id";
    }
    
    
}
