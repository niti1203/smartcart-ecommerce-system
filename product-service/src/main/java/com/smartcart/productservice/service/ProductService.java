package com.smartcart.productservice.service;

import com.smartcart.productservice.entity.Product;
import com.smartcart.productservice.exception.ProductNotFoundException;
import com.smartcart.productservice.dto.ProductDTO;
import com.smartcart.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product addProduct(Product product) {
        return repository.save(product);
    }

    public List<ProductDTO> getAllProducts() {

        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
    public List<ProductDTO> getProductsSortedByPrice() {

        return repository.findAll()
                .stream()
                .sorted(
                        (p1, p2) ->
                                Double.compare(
                                        p1.getPrice(),
                                        p2.getPrice()
                                )
                )
                .map(this::convertToDTO)
                .toList();
    }
    public List<ProductDTO> getExpensiveProducts() {

        return repository.findAll()
                .stream()
                .filter(product ->
                        product.getPrice() > 50000
                )
                .map(this::convertToDTO)
                .toList();
    }
    public List<String> getProductNames() {

        return repository.findAll()
                .stream()
                .map(Product::getName)
                .toList();
    }
    public Product getProductById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );
    }
    public Product updateProduct(Long id, Product updatedProduct) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setQuantity(updatedProduct.getQuantity());

        return repository.save(product);
    }

    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        repository.delete(product);
    }
    private ProductDTO convertToDTO(Product product) {

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}