package com.orders.service;

import com.orders.dtos.GetOrderDTO;
import com.orders.dtos.PostOrderDTO;
import com.orders.entity.Order;
import com.orders.exceptions.BadRequestException;
import com.orders.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class OrderService {
    private final Random random = new Random();

    private final OrderRepository orderRepository;

    private final KafkaTemplate<String, PostOrderDTO> kafkaTemplateOrder;

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<Order> findByType(String type) {
        return orderRepository.findByType(type);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "test-order-processed", partitions = { "1" }), containerFactory = "orderKafkaListenerContainerFactory")
    public void orderListener(GetOrderDTO order) {
        System.out.println("Received Message Consumer 01: " + order.getName());
    }

    public Order addOrder(PostOrderDTO orderDTO) {
        int partition = random.nextInt(2);
        Order order = new Order();
        order.setName(orderDTO.getName());
        order.setDescription(orderDTO.getDescription());
        order.setType(orderDTO.getType());
        order.setPrice(orderDTO.getPrice());

        System.out.println("Sending Order: " + orderDTO.getName());
        kafkaTemplateOrder.send("test-order-processed",partition, null, orderDTO);
        return orderRepository.save(order);
    }

    public Order deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Order not found"));
        orderRepository.delete(order);
        return order;
    }
}
