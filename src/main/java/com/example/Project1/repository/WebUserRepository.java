package com.example.Project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.WebUser;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Integer> {
        public WebUser findByEmail(String email);

}
