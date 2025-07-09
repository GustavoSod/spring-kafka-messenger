package com.orders.util;

import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;

import java.math.BigDecimal;

public class OrderCreator {

    public static Order createOrderToBeSaved(){
        return Order.builder().name("Coca Cola").type("food").price(new BigDecimal("10.0")).description("2L zero açucar").build();
    }

    public static PostOrderDTO postOrderToBeSaved(){
        return PostOrderDTO.builder().name("Coca Cola").type("food").price(new BigDecimal("10.0")).description("2L zero açucar").build();
    }

    public static Order createValidOrder(){
        return Order.builder().name("Coca Cola").type("food").price(new BigDecimal("10.0")).description("2L zero açucar").id(1L).build();
    }

    public static Order createValidUpdateOrder(){
        return Order.builder().name("Pepsi").type("food").price(new BigDecimal("10.0")).description("2L zero açucar").id(1L).build();
    }
}
