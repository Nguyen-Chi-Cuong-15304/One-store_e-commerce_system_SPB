package com.example.Project1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.Project1.entity.Cart;
import com.example.Project1.entity.WebUser;
import com.example.Project1.modals.WebUserDto;
import com.example.Project1.repository.CartRepository;
import com.example.Project1.repository.WebUserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class AccountController {
    
    @Autowired
    private WebUserRepository repo;

    @Autowired
    private CartRepository cartRepo;

    @GetMapping("/register")
    public String register(Model model) {
        WebUserDto webUserDto = new WebUserDto();
        model.addAttribute(webUserDto);
        model.addAttribute("success", false);
        return "register";
    }
    @GetMapping("/login")
    public String login(Model model) {
        // RegisterDto registerDto = new RegisterDto();
        // model.addAttribute(registerDto);
        // model.addAttribute("success", false);
        WebUserDto webUserDto = new WebUserDto();
        model.addAttribute(webUserDto);
        return "login";
    }
    

    @PostMapping(value = "/register")
    public String register(@Valid @ModelAttribute WebUserDto webUserDto, Model model, BindingResult result){
        if(!webUserDto.getPassword().equals(webUserDto.getConfirmPassword())){
            result.addError(new FieldError("webUserDto", "confirmPassword", "Password and Confirmpassword do not match"));
        }
        WebUser nWebUserser=  repo.findByEmail(webUserDto.getEmail());
        if(nWebUserser!= null){
            result.addError(new FieldError("webUserDto", "email", "Email address is already used"));
        }
        if(result.hasErrors()){
            return "register";
        }
        try{
            var bCryptPasswordEncoder = new BCryptPasswordEncoder();
            WebUser nUser = new WebUser();
            nUser.setFullName(webUserDto.getFullName());
            nUser.setAddress(webUserDto.getAddress());
            nUser.setEmail(webUserDto.getEmail());
            nUser.setPassword(bCryptPasswordEncoder.encode(webUserDto.getPassword()));
            nUser.setPhoneNumber(webUserDto.getPhoneNumber());
            nUser.setUsername(webUserDto.getUsername());
            nUser.setRoles("customer");

            repo.save(nUser);
            model.addAttribute("webUserDto", new WebUserDto());
            model.addAttribute("success", true); 
            Cart cart = new Cart();
            WebUser webUser = repo.findByEmail(webUserDto.getEmail());
            cart.setUserID(webUser.getUserID());
            cartRepo.save(cart);

        }
        catch(Exception exception){
            result.addError(new FieldError("webUserDto", "fullName", exception.getMessage()));
        }

        return "login";
    }
    // @PostMapping(value = "/mylogin")
    // public String login(@ModelAttribute("registerDto") RegisterDto registerDto, Model model){
    //     WebUser webUser = repo.findByEmail(registerDto.getEmail());
    //     if(webUser == null){
    //             return "mylogin";
    //     }
    //     BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //     if(passwordEncoder.matches(registerDto.getPassword(), webUser.getPassword())){
    //             model.addAttribute("username", webUser.getEmail());
    //             return "index";
    //     }
    //     return "prd";
    // }
    

}
