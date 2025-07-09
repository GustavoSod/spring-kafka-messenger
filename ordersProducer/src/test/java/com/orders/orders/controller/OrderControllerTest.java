package com.orders.controller;

import com.orders.dtos.GetOrderDTO;
import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;
import com.orders.service.OrderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp(){
        PageImpl<Order> orderAll = new PageImpl<>(List.of(OrderCreator.createValidOrder()));
        BDDMockito.when(orderService.findAll(ArgumentMatchers.any()))
                .thenReturn(orderAll);

        BDDMockito.when(orderService.findByType(ArgumentMatchers.any()))
                .thenReturn(List.of(OrderCreator.createValidOrder()));

        BDDMockito.when(orderService.addOrder(ArgumentMatchers.any(PostOrderDTO.class)))
                .thenReturn(OrderCreator.createOrderToBeSaved());

        BDDMockito.when(orderService.deleteOrder(ArgumentMatchers.any()))
                .thenReturn(OrderCreator.createValidOrder());

    }

    @Test
    @DisplayName("Return a list of orders inside page object when successful")
    void getAllOrdersInsidePageObject(){
        String expectedName = OrderCreator.createValidOrder().getName();
        Page<GetOrderDTO> orderPage = orderController.getAllOrders(null).getBody();

        Assertions.assertThat(orderPage).isNotNull();
        Assertions.assertThat(orderPage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(orderPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Returns a list of orders found by type")
    void findOrderByType(){
        List<Order> ordersByType = orderController.findByType("food").getBody();

        Assertions.assertThat(ordersByType).isNotNull();
        Assertions.assertThat(ordersByType)
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(ordersByType.get(0).getType()).isEqualTo("food");
    }

    @Test
    @DisplayName("Post/Create returns a Order when successful ")
    void createPostNewOrder(){

        PostOrderDTO orderCreated = OrderCreator.postOrderToBeSaved();
        ResponseEntity<Order> response = orderController.postOrder(orderCreated);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(orderCreated.getName());
        Assertions.assertThat(response.getBody().getDescription()).isEqualTo(orderCreated.getDescription());
        Assertions.assertThat(response.getBody().getPrice()).isEqualTo(orderCreated.getPrice());

    }

    @Test
    @DisplayName("Delete returns a Order when successful ")
    void deleteOrder(){

        Order orderCreated = OrderCreator.createValidOrder();
        Long orderId = orderCreated.getId();

        ResponseEntity<Order> response = orderController.deleteOrder(orderId);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo(orderCreated.getName());
        Assertions.assertThat(response.getBody().getDescription()).isEqualTo(orderCreated.getDescription());
        Assertions.assertThat(response.getBody().getPrice()).isEqualTo(orderCreated.getPrice());
    }
}