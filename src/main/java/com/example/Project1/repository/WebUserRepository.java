package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.WebUser;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Integer> {
        public WebUser findByEmail(String email);

        @Query("SELECT COUNT(w) FROM WebUser w")
        public int countTotalCustomers();

        @Query("SELECT w FROM WebUser w ORDER BY w.totalCost DESC")
        public List<WebUser> findByTotalCostDesc();
}
