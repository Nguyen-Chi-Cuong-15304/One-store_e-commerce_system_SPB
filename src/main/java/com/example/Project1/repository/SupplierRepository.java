package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    public Supplier findBySupplierName(String supplierName);

    @Query("SELECT s FROM Supplier s ORDER BY s.supplierID  DESC")
    List<Supplier> findTop3Suppliers(PageRequest pageRequest);

    
}