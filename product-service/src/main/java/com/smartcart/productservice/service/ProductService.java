package com.smartcart.productservice.service;

import com.smartcart.productservice.dto.ProductDTO;
import com.smartcart.productservice.entity.Product;
import com.smartcart.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
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
                .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                .map(this::convertToDTO)
                .toList();
    }

    public ProductDTO addProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product saved = repository.save(product);
        return convertToDTO(saved);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        return dto;
    }
}