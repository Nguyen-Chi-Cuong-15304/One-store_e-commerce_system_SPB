package com.example.Project1.modals;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProductDto {
    // @NotNull
    // private int productID;

    @NotEmpty
    private String productName;

    @NotEmpty
    private String description;

    @NotNull 
    private BigDecimal price;

    @NotNull
    private int supplierID;

   
    private MultipartFile linkImg;

    @NotNull 
    private int stockQuantity;

    @NotEmpty
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

 

 

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public MultipartFile getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(MultipartFile linkImg) {
        this.linkImg = linkImg;
    }

}
