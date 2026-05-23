package com.smartcart.orderservice.controller;

import com.smartcart.orderservice.dto.OrderRequestDTO;
import com.smartcart.orderservice.entity.Order;
import com.smartcart.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public Order placeOrder(
            @RequestBody OrderRequestDTO requestDTO
    ) {

        return orderService.placeOrder(
                requestDTO
        );
    }
    @DeleteMapping("/delete/{id}")
    public String deleteOrder(
            @PathVariable Integer id
    ) {

        orderService.deleteOrder(id);

        return "Order deleted successfully";
    }

}