package com.orders.integration;

import com.orders.dtos.GetOrderDTO;
import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;
import com.orders.repository.OrderRepository;
import com.orders.util.OrderCreator;
import com.orders.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class OrderControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Return a list of orders inside page object when successful")
    void getAllOrdersInsidePageObject(){
        Order orderSaved = orderRepository.save(OrderCreator.createOrderToBeSaved());
        String expectedName = orderSaved.getName();

        PageableResponse<Order> orderPage = testRestTemplate.exchange("/orders", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Order>>() {
                }).getBody() ;

        Assertions.assertThat(orderPage).isNotNull();
        Assertions.assertThat(orderPage.toList())
                .isNotEmpty()
                .hasSizeGreaterThan(0);
        Assertions.assertThat(orderPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Returns a list of orders found by type")
    void findOrderByType(){

        Order orderSaved = orderRepository.save(OrderCreator.createOrderToBeSaved());
        String expectedType = orderSaved.getType();
        String url = String.format("/orders/find?type=%s", expectedType);

        List<Order> ordersByType = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {
                }).getBody() ;

        Assertions.assertThat(ordersByType).isNotNull();
        Assertions.assertThat(ordersByType)
                .isNotEmpty()
                .hasSizeGreaterThan(0);
        Assertions.assertThat(ordersByType.get(0).getType()).isEqualTo("food");
    }

    @Test
    @DisplayName("Post/Create returns a Order when successful ")
    void createPostNewOrder(){

        PostOrderDTO orderPostBoyd = OrderCreator.postOrderToBeSaved();
        ResponseEntity<Order> orderResponseEntity = testRestTemplate.postForEntity("/orders", orderPostBoyd , Order.class);

        Assertions.assertThat(orderResponseEntity).isNotNull();
        Assertions.assertThat(orderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(orderResponseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Delete returns a Order when successful ")
    void deleteOrder(){

        Order orderSaved = orderRepository.save(OrderCreator.createOrderToBeSaved());
        Long expectedId = orderSaved.getId();

        ResponseEntity<Void> orderResponseEntity = testRestTemplate.exchange("/orders/{id}",
                    HttpMethod.DELETE, null, Void.class, expectedId);

        Assertions.assertThat(orderResponseEntity).isNotNull();
        Assertions.assertThat(orderResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
