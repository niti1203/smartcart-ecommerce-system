package com.smartcart.orderservice.service;

import com.smartcart.orderservice.dto.OrderRequestDTO;
import com.smartcart.orderservice.dto.ProductDTO;
import com.smartcart.orderservice.entity.Order;
import com.smartcart.orderservice.feign.ProductFeignClient;
import com.smartcart.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;

    private static final Logger logger =
            LoggerFactory.getLogger(OrderService.class);

    public OrderService(
            OrderRepository orderRepository,
            ProductFeignClient productFeignClient
    ) {
        this.orderRepository = orderRepository;
        this.productFeignClient = productFeignClient;
    }

    @CircuitBreaker(
            name = "productService",
            fallbackMethod = "fallbackOrder"
    )
    public Order placeOrder(
            OrderRequestDTO requestDTO
    ) {

        logger.info(
                "Placing order for product id: {}",
                requestDTO.getProductId()
        );

        ProductDTO product =
                productFeignClient.getProductById(
                        requestDTO.getProductId()
                );

        logger.info(
                "Fetched product: {}",
                product.getName()
        );

        Order order = new Order();

        order.setId(requestDTO.getId());

        order.setProductId(
                product.getId()
        );

        order.setProductName(
                product.getName()
        );

        order.setQuantity(
                requestDTO.getQuantity()
        );

        order.setPrice(
                product.getPrice()
                        * requestDTO.getQuantity()
        );

        order.setUsername(
                requestDTO.getUsername()
        );

        logger.info(
                "Saving order for user: {}",
                requestDTO.getUsername()
        );

        return orderRepository.save(order);
    }

    public void deleteOrder(Integer id) {

        orderRepository.deleteById(id);

        logger.info(
                "Order deleted successfully with id: {}",
                id
        );
    }

    public Order fallbackOrder(
            OrderRequestDTO requestDTO,
            Exception ex
    ) {

        logger.error(
                "Product Service is unavailable: {}",
                ex.getMessage()
        );

        Order order = new Order();

        order.setId(
                requestDTO.getId()
        );

        order.setProductId(
                requestDTO.getProductId()
        );

        order.setProductName(
                "Product Service Unavailable"
        );

        order.setQuantity(
                requestDTO.getQuantity()
        );

        order.setPrice(0.0);

        order.setUsername(
                requestDTO.getUsername()
        );

        return order;
    }
}