package com.example.Project1.controller.OrderController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Project1.repository.CategoryRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.ProductRepository;

import com.example.Project1.entity.Orders;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/orderManagement")
    public String orderManagement(Model model, @RequestParam(required = false) String month, @RequestParam(required = false, defaultValue = "All") String status) {
        if(month == null) {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            List<Orders> orders;
            if("All".equals(status)) {
                orders = orderRepository.findByMonthAndYear(currentMonth, currentYear);
            } else {
                orders = orderRepository.findByMonthAndYearAndOrderStatus(currentMonth, currentYear, status);
            }
            model.addAttribute("orders", orders);
            model.addAttribute("month", month);
            model.addAttribute("status", status);
            return "admin/orderManage";
        }
        

        String[] monthYear = month.split("-");
        int monthInt = Integer.parseInt(monthYear[1]);
        int year = Integer.parseInt(monthYear[0]);
        List<Orders> orders ;
        if("All".equals(status)) {
            orders = orderRepository.findByMonthAndYear(monthInt, year);
        }
        else {
            orders = orderRepository.findByMonthAndYearAndOrderStatus(monthInt, year, status);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);
        model.addAttribute("status", status);
        return "admin/orderManage";
    }

    @PostMapping("/orderManagement/findByMonth")
    public String String (Model model, @RequestParam String month, @RequestParam String status) {
        
        String[] monthYear = month.split("-");
        int monthInt = Integer.parseInt(monthYear[1]);
        int year = Integer.parseInt(monthYear[0]);
        List<Orders> orders ;
        if("All".equals(status)) {
            orders = orderRepository.findByMonthAndYear(monthInt, year);
        } else {
            orders = orderRepository.findByMonthAndYearAndOrderStatus(monthInt, year, status);
        }
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);
        model.addAttribute("status", status);
        String encodeStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
        return "redirect:/admin/order/orderManagement?month=" + month+ "&status=" + encodeStatus;
    }
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam int id, @Param("status_order") String status_order, Model model,@RequestParam String month, @RequestParam("curr_status") String status) {
        Orders order = orderRepository.findById(id).get();
        order.setStatus(status_order);
        orderRepository.save(order);
        model.addAttribute("month", month);
        String current_status = URLEncoder.encode(status, StandardCharsets.UTF_8);
        return "redirect:/admin/order/orderManagement?month=" + month+ "&status=" + current_status;
    }

    @GetMapping("/stats")
    public String getDashboardStats(Model model) {
        
        return "admin/adminDashboard";
    }
    
}
