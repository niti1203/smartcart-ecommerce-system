package com.smartcart.orderservice.repository;

import com.smartcart.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Integer> {
}