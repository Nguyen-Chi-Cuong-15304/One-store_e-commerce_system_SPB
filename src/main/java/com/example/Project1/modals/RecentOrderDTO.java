package com.example.Project1.modals;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentOrderDTO {
    private int orderid;
    private String address;
    private String name;
    private double total_amount;
    private String status;
}