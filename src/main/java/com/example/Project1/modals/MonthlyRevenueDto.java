package com.example.Project1.modals;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyRevenueDto {

    private int month;
    private double revenue;

}