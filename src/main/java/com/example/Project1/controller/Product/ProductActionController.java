package com.example.Project1.controller.Product;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project1.entity.Product;
import com.example.Project1.modals.ProductDto;
import com.example.Project1.repository.ProductRepository;
import com.example.Project1.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/product")
public class ProductActionController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping({"/", ""})
    public String showProduct(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/show_product";
    }
    @GetMapping("/add")
    public String add(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "product/add";
    }
    @PostMapping("/add")
    public String postAdd(@ModelAttribute("productDto") ProductDto productDto, Model model, BindingResult bindingResult) {
        
        if(productDto.getProductName().isEmpty()){
            bindingResult.addError(new FieldError("productDto", "productName", "Product name is required"));
        }
        if(productDto.getLinkImg().isEmpty()){
            bindingResult.addError(new FieldError("productDto", "linkImg", "Link image is required"));
        }
        if(bindingResult.hasErrors()){
            return "product/add";
        }
        MultipartFile file = productDto.getLinkImg();
        Date createTime = new Date(System.currentTimeMillis());
        String fileName = createTime.getTime() + file.getOriginalFilename();
        try {
            String uploadDir ="public/product_image/";
            Path   uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir+ fileName), StandardCopyOption.REPLACE_EXISTING);

            }
        }
        catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
        }
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setSupplierID(productDto.getSupplierID());
        product.setLinkImg(fileName);
        product.setCategory(productDto.getCategory());
        product.setStockQuantity(productDto.getStockQuantity());
        productRepository.save(product);
        //TODO: process POST request
        
        return "redirect:/product";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setSupplierID(product.getSupplierID());
        productDto.setLinkImg(null);
        productDto.setCategory(product.getCategory());
        productDto.setStockQuantity(product.getStockQuantity());
        model.addAttribute("productDto", productDto);
        return "product/edit";
    }

    @PostMapping("/edit")
    public String edit(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult) {
        try{
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            if(bindingResult.hasErrors()){
                return "product/edit";
            }

            if(!productDto.getLinkImg().isEmpty()){
                String uploadDir = "public/product_image/";
                Path oldPath = Paths.get(uploadDir + product.getLinkImg());

                try{
                    Files.delete(oldPath);
                }
                catch (Exception e){
                    System.out.println("Exception: " + e.getMessage());
                }
                MultipartFile file = productDto.getLinkImg();
                Date createTime = new Date(System.currentTimeMillis());
                String fileName = createTime.getTime() + file.getOriginalFilename();
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception e){
                    System.out.println("Exception: " + e.getMessage());
                }

                product.setLinkImg(fileName);
            }
            product.setProductName(productDto.getProductName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setSupplierID(productDto.getSupplierID());
            product.setCategory(productDto.getCategory());
            product.setStockQuantity(productDto.getStockQuantity());
            productRepository.save(product);

        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return "redirect:/product";
    }
    
    @GetMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        Product product = productRepository.findById(id).get();
        String uploadDir = "public/product_image/";
        Path oldPath = Paths.get(uploadDir + product.getLinkImg());
        try{
            Files.delete(oldPath);
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        productRepository.deleteById(id);
        return "redirect:/product";
    }
    
}
