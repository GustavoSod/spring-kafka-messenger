package com.orders.controller;

import com.orders.dtos.GetOrderDTO;
import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;
import com.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<GetOrderDTO>> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderService.findAll(pageable);

        Page<GetOrderDTO> dtoPage = orders.map(order -> {
            GetOrderDTO dto = new GetOrderDTO();
            dto.setName(order.getName());
            dto.setDescription(order.getDescription());
            dto.setPrice(order.getPrice());
            return dto;
        });

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Order>> findByType(@RequestParam String type) {
        List<Order> orders = orderService.findByType(type);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<Order> postOrder(@RequestBody @Valid PostOrderDTO orderDTO) {
        Order createdOrder = orderService.addOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id){
        Order deletedOrder = orderService.deleteOrder(id);
        return ResponseEntity.ok(deletedOrder);
    }
}
