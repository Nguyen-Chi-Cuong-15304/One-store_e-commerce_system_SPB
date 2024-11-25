package com.example.Project1.controller.Payment;

import java.lang.ProcessBuilder.Redirect;
import java.math.BigDecimal;
import java.util.Date;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Project1.entity.CartWrapper;
import com.example.Project1.entity.Orders;
import com.example.Project1.entity.OrderItem;
import com.example.Project1.entity.OrderItemWrapper;
import com.example.Project1.entity.WebUser;
import com.example.Project1.repository.OrderItemRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.WebUserRepository;
import com.example.Project1.service.VNPAYService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PaymentController {
    @Autowired
    private VNPAYService vnPayService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private WebUserRepository userRepository;

    // @GetMapping({"", "/"})
    // public String home(){
    //     return "createOrder";
    // }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(
                            
                            HttpServletRequest request, @ModelAttribute("orderItemWrapper") OrderItemWrapper orderItemWrapper, @ModelAttribute("cartWrapper") CartWrapper cartWrapper){
        //Lưu thông tin đơn hàng vào database
                org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        int userID = user.getUserID();
        Orders order = orderItemWrapper.getOrder();
        order.setUserID(userID);
        order.setStatus("Đã thanh toán");
        order.setOrderDate(new Date(System.currentTimeMillis()));
        
        Date date = order.getOrderDate();
        orderRepository.save(order);
        Orders order1 = orderRepository.findByOrderDate(date);
        int orderID = order1.getOrderID();
        
        BigDecimal totalAmount = new BigDecimal(0);
        for(OrderItem orderItem : orderItemWrapper.getOrderItems()){
            orderItem.setOrderID(orderID);
            orderItemRepository.save(orderItem);
            totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        String orderInfo = "Thanh toan don hang " + orderID;
        int orderTotal = totalAmount.multiply(new BigDecimal(1000)).intValue();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}