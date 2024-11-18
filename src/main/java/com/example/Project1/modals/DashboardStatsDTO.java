package com.example.Project1.modals;

import java.util.List;

import com.example.Project1.entity.Orders;
import com.example.Project1.entity.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {
    private double totalRevenue;
    private int totalOrders;
    private int totalCustomers;
    private double conversionRate;
    private List<MonthlyRevenueDto> revenueChart;
    private List<TopProductDTO> topProducts;
    private List<RecentOrderDTO> recentOrders;
}
