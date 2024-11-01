package com.example.Project1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "product_category")
@IdClass(ProductCategoryKey.class)
public class Product_Category {

    @Id
    private int productID;

    @Id
    private int categoryID;

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}

// Class đại diện cho khóa chính
class ProductCategoryKey implements Serializable {

    private int productID;
    private int categoryID;

    // Constructors
    public ProductCategoryKey() {}

    public ProductCategoryKey(int productID, int categoryID) {
        this.productID = productID;
        this.categoryID = categoryID;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryKey that = (ProductCategoryKey) o;
        return productID == that.productID && categoryID == that.categoryID;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(productID, categoryID);
    }
}
