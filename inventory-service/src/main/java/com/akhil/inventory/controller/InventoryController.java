package com.akhil.inventory.controller;

import com.akhil.inventory.model.Product;
import com.akhil.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return inventoryService.addProduct(product);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return inventoryService.getProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Called by Order Service before confirming an order
    @GetMapping("/products/{id}/check")
    public ResponseEntity<Boolean> checkStock(@PathVariable Long id, @RequestParam int qty) {
        return ResponseEntity.ok(inventoryService.isInStock(id, qty));
    }

    // Called by Order Service after an order is confirmed
    @PostMapping("/products/{id}/decrement")
    public ResponseEntity<Boolean> decrementStock(@PathVariable Long id, @RequestParam int qty) {
        boolean success = inventoryService.decrementStock(id, qty);
        return success ? ResponseEntity.ok(true) : ResponseEntity.badRequest().body(false);
    }
}
