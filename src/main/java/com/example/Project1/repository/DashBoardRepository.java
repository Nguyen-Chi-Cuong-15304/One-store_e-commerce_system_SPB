package com.example.Project1.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Product;
import com.example.Project1.modals.MonthlyRevenueDto;
import com.example.Project1.modals.TopProductDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DashBoardRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<MonthlyRevenueDto> getMonthlyRevenue(int year) {
        String sql = """
            SELECT MONTH(o.order_date) as month,
                   SUM(o.total_amount) as revenue
            FROM orders o
            WHERE YEAR(o.order_date) = :year
            GROUP BY MONTH(o.order_date)
            ORDER BY month
        """;
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(sql);
        query.setParameter("year", year);
        
        List<Object[]> results = query.getResultList();
        return results.stream()
            .map(result -> MonthlyRevenueDto.builder()
                .month(((Number) result[0]).intValue())
                .revenue(((Number) result[1]).doubleValue())
                .build())
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<TopProductDTO> getTopProducts() {
        String sql = """
            SELECT p.productid, p.product_name,
                   COUNT(oi.productid) as quantity,
                   SUM(oi.price * oi.quantity) as revenue
            FROM product p
            JOIN orderitem oi ON p.productid = oi.productid
            GROUP BY p.productid, p.product_name
            ORDER BY revenue DESC
            LIMIT 5
        """;
        
        jakarta.persistence.Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        
        return results.stream()
            .map(result -> TopProductDTO.builder()
                .id(((Number) result[0]).longValue())
                .name((String) result[1])
                .quantity(((Number) result[2]).intValue())
                .revenue(((Number) result[3]).doubleValue())
                .build())
            .collect(Collectors.toList());
    }
}
