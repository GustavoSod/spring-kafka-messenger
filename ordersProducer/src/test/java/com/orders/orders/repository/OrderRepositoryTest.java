package com.orders.repository;

import com.orders.entity.Order;
import com.orders.util.OrderCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Test for Order Repository")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Create Order when Successful")
    void createOrderTest(){
        Order order = OrderCreator.createOrderToBeSaved();
        Order orderSaved = this.orderRepository.save(order);
        Assertions.assertThat(orderSaved).isNotNull();
        Assertions.assertThat(orderSaved.getName()).isEqualTo(order.getName());
    }

    @Test
    @DisplayName("Should throw ConstraintViolationException when name is blank")
    void constraintViolationExceptionOrderTest(){
        Order order = new Order();

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.orderRepository.saveAndFlush(order))
                .withMessageContaining("Name cannot be blank");
    }

    @Test
    @DisplayName("Save update Order when Successful")
    void updateOrderTest(){
        Order order = OrderCreator.createOrderToBeSaved();
        Order orderSaved = this.orderRepository.save(order);

        orderSaved.setName("Guaraná");
        Order orderUpdated = this.orderRepository.save(orderSaved);

        Assertions.assertThat(orderUpdated).isNotNull();
        Assertions.assertThat(orderSaved.getName()).isEqualTo(order.getName());
    }

    @Test
    @DisplayName("Delete removes Order when Successful")
    void deleteOrderTest(){
        Order order = OrderCreator.createOrderToBeSaved();
        Order orderSaved = this.orderRepository.save(order);

        this.orderRepository.delete(orderSaved);
        Optional<Order> orderOptional = this.orderRepository.findById(orderSaved.getId());

        Assertions.assertThat(orderOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Name return Order list when Successful")
    void findByTypeOrderTest(){
        Order order = OrderCreator.createOrderToBeSaved();
        Order orderSaved = this.orderRepository.save(order);

        String orderType = orderSaved.getType();
        List<Order> orders = this.orderRepository.findByType(orderType);


        Assertions.assertThat(orders)
                .isNotEmpty()
                .contains(orderSaved);
    }

    @Test
    @DisplayName("Find By Name return Order empty list when no Order is found")
    void findByTypeEmptyOrderTest(){
        List<Order> orders = this.orderRepository.findByType("sweet");
        Assertions.assertThat(orders).isEmpty();
    }
}

