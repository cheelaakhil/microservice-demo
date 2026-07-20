package com.akhil.inventory.service;

import com.akhil.inventory.model.Product;
import com.akhil.inventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Checks if requested quantity is available. Called by Order Service
     * before an order is confirmed.
     */
    public boolean isInStock(Long productId, int requestedQty) {
        return productRepository.findById(productId)
                .map(p -> p.getQuantity() >= requestedQty)
                .orElse(false);
    }

    /**
     * Decrements stock after an order is confirmed. In a production system
     * this would be wrapped in a distributed-transaction-safe pattern
     * (e.g. Saga), but for this demo a simple synchronous decrement is fine.
     */
    public boolean decrementStock(Long productId, int qty) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return false;
        }
        Product product = productOpt.get();
        if (product.getQuantity() < qty) {
            return false;
        }
        product.setQuantity(product.getQuantity() - qty);
        productRepository.save(product);
        return true;
    }
}
