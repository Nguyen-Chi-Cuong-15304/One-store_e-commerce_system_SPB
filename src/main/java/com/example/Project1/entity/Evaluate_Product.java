package com.example.Project1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evaluate_product")
public class Evaluate_Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int evaluateID;

    private int productID;
    private String evaluateContent;
    public int getEvaluateID() {
        return evaluateID;
    }
    public void setEvaluateID(int evaluateID) {
        this.evaluateID = evaluateID;
    }
    public int getProductID() {
        return productID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }
    public String getEvaluateContent() {
        return evaluateContent;
    }
    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }
}
