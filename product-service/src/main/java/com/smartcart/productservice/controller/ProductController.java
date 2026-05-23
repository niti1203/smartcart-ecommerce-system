package com.smartcart.productservice.controller;

import com.smartcart.productservice.dto.ProductDTO;
import com.smartcart.productservice.entity.Product;
import com.smartcart.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // USER + ADMIN
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return service.getAllProducts();
    }
    @GetMapping("/{id}")
    public Product getProductById(
            @PathVariable Long id
    ) {
        return service.getProductById(id);
    }
    @GetMapping("/sorted")
    public List<ProductDTO> getSortedProducts() {

        return service.getProductsSortedByPrice();
    }
    @GetMapping("/expensive")
    public List<ProductDTO> getExpensiveProducts() {

        return service.getExpensiveProducts();
    }
    @GetMapping("/names")
    public List<String> getProductNames() {

        return service.getProductNames();
    }

    // ADMIN ONLY
    @PostMapping("/add")
    public Product addProduct(
            @Valid @RequestBody Product product) {

        return service.addProduct(product);
    }

    // ADMIN ONLY
    @PutMapping("/update/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.updateProduct(id, product);
    }

    // ADMIN ONLY
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        service.deleteProduct(id);

        return "Product deleted successfully";
    }
}