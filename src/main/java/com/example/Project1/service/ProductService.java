package com.example.Project1.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project1.entity.Product;
import com.example.Project1.modals.ProductDto;
import com.example.Project1.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public void addProduct(ProductDto productDto, BindingResult bindingResult, Product product){ 
        if(productDto.getProductName().isEmpty()){
            bindingResult.addError(new FieldError("productDto", "productName", "Product name is required"));
        }
        if(productDto.getLinkImg().isEmpty()){
            bindingResult.addError(new FieldError("productDto", "linkImg", "Link image is required"));
        }
        if(bindingResult.hasErrors()){
            return;
        }
        MultipartFile file = productDto.getLinkImg();
        Date createTime = new Date(System.currentTimeMillis());
        String fileName = createTime.getTime() + file.getOriginalFilename();
        try {
            String uploadDir ="public/images/";
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
        
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setSupplierID(productDto.getSupplierID());
        product.setLinkImg(fileName);
        product.setCategory(productDto.getCategory());
        product.setStockQuantity(productDto.getStockQuantity());
        productRepository.save(product);
       
        
    }

}
