package com.orders.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetOrderDTO {
    private String name;
    private String description;
    private BigDecimal price;
}
