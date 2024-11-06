package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

}