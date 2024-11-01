package com.example.Project1.modals;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {
    @NotEmpty
    private int categoryID;
    @NotEmpty
    private String categoryName;
    
    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
