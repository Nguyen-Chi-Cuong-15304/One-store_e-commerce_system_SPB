package com.example.Project1.controller.Payment;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.Project1.entity.CartWrapper;
import com.example.Project1.entity.Orders;
import com.example.Project1.entity.Product;
import com.example.Project1.entity.OrderItem;
import com.example.Project1.entity.OrderItemWrapper;
import com.example.Project1.entity.WebUser;
import com.example.Project1.repository.OrderItemRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.ProductRepository;
import com.example.Project1.repository.WebUserRepository;
import com.example.Project1.service.VNPAYService;
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

    @Autowired
    private ProductRepository productRepository;

    // @GetMapping({"", "/"})
    // public String home(){
    //     return "createOrder";
    // }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(   
                            HttpServletRequest request, @ModelAttribute OrderItemWrapper orderItemWrapper, @ModelAttribute CartWrapper cartWrapper){
        
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        String email = auth.getName();
        WebUser user = userRepository.findByEmail(email);
        // Tính tổng tiền và chuẩn bị dữ liệu
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemWrapper.getOrderItems()) {
            totalAmount = totalAmount.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }

        // Tạo URL VNPAY
        String orderInfo = "Thanh toan don hang cua user " + user.getUserID();
        int orderTotal = totalAmount.multiply(new BigDecimal(1000)).intValue();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);

        // Lưu thông tin đơn hàng tạm thời vào session (hoặc database với trạng thái Pending)
        request.getSession().setAttribute("orderItemWrapper", orderItemWrapper);
        request.getSession().setAttribute("totalAmount", totalAmount);
        request.getSession().setAttribute("userID", user.getUserID());
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);

        // Lấy thông tin từ session
        OrderItemWrapper orderItemWrapper = (OrderItemWrapper) request.getSession().getAttribute("orderItemWrapper");
        BigDecimal totalAmount = (BigDecimal) request.getSession().getAttribute("totalAmount");
        int userID = (int) request.getSession().getAttribute("userID");
         if(paymentStatus == 1){
            Orders order = orderItemWrapper.getOrder();
            order.setUserID(userID);
            order.setStatus("Đã thanh toán");
            order.setOrderDate(new Date(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            orderRepository.save(order);

            for (OrderItem orderItem : orderItemWrapper.getOrderItems()) {
                orderItem.setOrderID(order.getOrderID());
                orderItemRepository.save(orderItem);
                Product product = productRepository.findById(orderItem.getProductID()).get();
                product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
                productRepository.save(product);
            }
         }

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