package com.example.Project1.modals;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopProductDTO {
    private Long id;
    private String name;
    private int quantity;
    private double revenue;
}
