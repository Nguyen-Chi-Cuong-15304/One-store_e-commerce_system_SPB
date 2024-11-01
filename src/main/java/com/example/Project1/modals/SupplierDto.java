package com.example.Project1.modals;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;

public class SupplierDto {
    
    private int supplierID;

    @NotEmpty
    private String supplierName;

    @NotEmpty
    private String address;
    @NotEmpty
    private String phoneNumber;
    @NotEmpty
    private String email;
    @NotEmpty
    private String website;
    
    private MultipartFile linkImg;

    @NotEmpty
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getSupplierID() {
        return supplierID;
    }
    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }
    public MultipartFile getLinkImg() {
        return linkImg;
    }
    public void setLinkImg(MultipartFile linkImg) {
        this.linkImg = linkImg;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    } 
}
