package com.example.Project1.controller.UserInfoController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.Project1.entity.Orders;
import com.example.Project1.entity.OrderItem;
import com.example.Project1.entity.WebUser;
import com.example.Project1.repository.OrderItemRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.WebUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("id") String id) {
        return "userInfo";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody String password) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = webUserRepository.findByEmail(email);
        user.setPassword(password);
        webUserRepository.save(user);
        return "redirect:/userInfo/userInfo";
    }
    

    @GetMapping("/AllOrders")
    public String allOrders(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        WebUser user = webUserRepository.findByEmail(email);
        int userID = user.getUserID();
        List<Orders> orders = orderRepository.findByUserID(userID);
        model.addAttribute("orders", orders);
        return "user/user_list_order";

    }
    
    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam("id") int id, Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        model.addAttribute("userName", email);
        List<OrderItem> orderItems = orderItemRepository.findByOrderID(id);
        model.addAttribute("orderItems", orderItems);
        return "user/orderDetail";
    }
}
