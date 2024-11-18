package com.example.Project1.controller.OrderController;

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
    public String orderManagement(Model model, @RequestParam(value = "month", required = false) String month) {
        if(month == null) {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            List<Orders> orders = orderRepository.findByMonthAndYear(currentMonth, currentYear);
            model.addAttribute("orders", orders);
            model.addAttribute("month", currentYear + "-" + currentMonth);
            return "admin/orderManage";
        }
        String[] monthYear = month.split("-");
        int monthInt = Integer.parseInt(monthYear[1]);
        int year = Integer.parseInt(monthYear[0]);
        List<Orders> orders = orderRepository.findByMonthAndYear(monthInt, year);
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);
        return "admin/orderManage";
    }

    @PostMapping("/orderManagement/findByMonth")
    public String String (Model model, @RequestParam("month") String month) {
        String[] monthYear = month.split("-");
        int monthInt = Integer.parseInt(monthYear[1]);
        int year = Integer.parseInt(monthYear[0]);
        List<Orders> orders = orderRepository.findByMonthAndYear(monthInt, year);
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);
        return "redirect:/admin/order/orderManagement?month=" + month;
    }
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam int id, @Param("status") String status, Model model,@RequestParam String month) {
        Orders order = orderRepository.findById(id).get();
        order.setStatus(status);
        orderRepository.save(order);
        model.addAttribute("month", month);
        return "redirect:/admin/order/orderManagement?month=" + month;
    }

    @GetMapping("/stats")
    public String getDashboardStats(Model model) {
        
        return "admin/adminDashboard";
    }
    
}
