package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Category findBycategoryName(String categoryName);
}