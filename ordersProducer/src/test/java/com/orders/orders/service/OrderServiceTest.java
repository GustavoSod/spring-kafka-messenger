package com.orders.service;

import com.orders.controller.OrderController;
import com.orders.dtos.GetOrderDTO;
import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;
import com.orders.repository.OrderRepository;
import com.orders.util.OrderCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp(){
        PageImpl<Order> orderAll = new PageImpl<>(List.of(OrderCreator.createValidOrder()));
        BDDMockito.when(orderRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(orderAll);

        BDDMockito.when(orderRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(java.util.Optional.of(OrderCreator.createValidOrder()));

        BDDMockito.when(orderRepository.findByType(ArgumentMatchers.any()))
                .thenReturn(List.of(OrderCreator.createValidOrder()));

        BDDMockito.when(orderRepository.save(ArgumentMatchers.any(Order.class)))
                .thenReturn(OrderCreator.createOrderToBeSaved());

        BDDMockito.doNothing().when(orderRepository).delete(ArgumentMatchers.any(Order.class));
    }

    @Test
    @DisplayName("Return a list of orders inside page object when successful")
    void getAllOrdersInsidePageObject(){
        String expectedName = OrderCreator.createValidOrder().getName();
        Page<Order> orderPage = orderService.findAll(PageRequest.of(1,1));

        Assertions.assertThat(orderPage).isNotNull();
        Assertions.assertThat(orderPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(orderPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Returns a list of orders found by type")
    void findOrderById(){
        List<Order> ordersByType = orderService.findByType("food");

        Assertions.assertThat(ordersByType).isNotNull();
        Assertions.assertThat(ordersByType)
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(ordersByType.get(0).getType()).isEqualTo("food");
    }

    @Test
    @DisplayName("addOrder returns Order when successful")
    void addOrder_ReturnsOrder_WhenSuccessful() {
        PostOrderDTO postOrderDTO = OrderCreator.postOrderToBeSaved();

        Order savedOrder = orderService.addOrder(postOrderDTO);

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(savedOrder.getName()).isEqualTo(postOrderDTO.getName());
        Assertions.assertThat(savedOrder.getDescription()).isEqualTo(postOrderDTO.getDescription());
        Assertions.assertThat(savedOrder.getPrice()).isEqualTo(postOrderDTO.getPrice());
        Assertions.assertThat(savedOrder.getType()).isEqualTo(postOrderDTO.getType());
    }


    @Test
    @DisplayName("deleteOrder returns an Order when successful")
    void deleteOrder_WhenSuccessful() {
        Order orderCreated = OrderCreator.createValidOrder();
        Long orderId = orderCreated.getId();

        Order orderDeleted = orderService.deleteOrder(orderId);

        Assertions.assertThat(orderDeleted).isNotNull();
        Assertions.assertThat(orderDeleted.getName()).isEqualTo(orderCreated.getName());
        Assertions.assertThat(orderDeleted.getDescription()).isEqualTo(orderCreated.getDescription());
        Assertions.assertThat(orderDeleted.getPrice()).isEqualTo(orderCreated.getPrice());

        BDDMockito.verify(orderRepository).delete(ArgumentMatchers.any(Order.class));
    }

}