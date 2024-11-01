package com.example.Project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Project1.entity.WebUser;
import com.example.Project1.repository.WebUserRepository;

@Service
public class WebUserService implements UserDetailsService{
    @Autowired
    private WebUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        WebUser webUser = repo.findByEmail(email);
        if(webUser != null){
            var springUser  = User.withUsername(webUser.getEmail())
                .password(webUser.getPassword())
                .roles(webUser.getRoles())
                .build();
            return springUser;
        }
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    public void save(WebUser webUser) {
        repo.save(webUser);
    }
    
}
