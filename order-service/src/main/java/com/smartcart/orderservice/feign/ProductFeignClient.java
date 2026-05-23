package com.smartcart.orderservice.feign;

import com.smartcart.orderservice.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductFeignClient {

    @GetMapping("/products/{id}")
    ProductDTO getProductById(
            @PathVariable Integer id
    );
}