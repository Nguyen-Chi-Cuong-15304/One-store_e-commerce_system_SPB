package com.example.Project1.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project1.entity.Orders;
import com.example.Project1.modals.DashboardStatsDTO;
import com.example.Project1.modals.MonthlyRevenueDto;
import com.example.Project1.modals.RecentOrderDTO;
import com.example.Project1.modals.TopProductDTO;
import com.example.Project1.repository.DashBoardRepository;
import com.example.Project1.repository.OrderRepository;
import com.example.Project1.repository.WebUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    
    private final OrderRepository orderRepository;
    private final WebUserRepository customerRepository;
    private final DashBoardRepository dashboardRepository;

    public DashboardStatsDTO getDashboardStats() {
        int currentYear = Year.now().getValue();
        
        // Lấy các thông số cơ bản
        Double totalRevenue = orderRepository.calculateTotalRevenueForYear(currentYear);
        Integer totalOrders = orderRepository.countTotalOrdersForYear(currentYear);
        Integer totalCustomers = customerRepository.countTotalCustomers();
        
        // Tính tỷ lệ chuyển đổi
        double conversionRate = totalCustomers > 0 
            ? (double) totalOrders / totalCustomers * 100 
            : 0;
            
        // Lấy dữ liệu biểu đồ và báo cáo
        List<MonthlyRevenueDto> revenueChart = dashboardRepository.getMonthlyRevenue(currentYear);
        List<TopProductDTO> topProducts = dashboardRepository.getTopProducts();
        List<Orders> recentOrder = orderRepository.getRecentOrders();
        List<RecentOrderDTO> recentOrders = new ArrayList<>();
        for (Orders order : recentOrder) {
            recentOrders.add(RecentOrderDTO.builder()
                .orderid(order.getOrderID())
                .total_amount(order.getTotalAmount().floatValue())
                .name(order.getName())
                .status(order.getStatus())
                .address(order.getAddress())
                .build());
        }
        
        
        // Đóng gói tất cả dữ liệu
        return DashboardStatsDTO.builder()
            .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
            .totalOrders(totalOrders != null ? totalOrders : 0)
            .totalCustomers(totalCustomers != null ? totalCustomers : 0)
            .conversionRate(Math.round(conversionRate * 10.0) / 10.0)
            .revenueChart(revenueChart)
            .topProducts(topProducts)
            .recentOrders(recentOrders)
            .build();
    }
}
