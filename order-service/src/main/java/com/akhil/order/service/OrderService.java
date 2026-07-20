package com.akhil.order.service;

import com.akhil.order.client.InventoryClient;
import com.akhil.order.model.Order;
import com.akhil.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryClient inventoryClient;

    /**
     * Core cross-service flow:
     * 1. Ask Inventory Service if stock is available
     * 2. If yes, decrement stock on Inventory Service
     * 3. Save the order locally with CONFIRMED / OUT_OF_STOCK status
     */
    public Order placeOrder(Long productId, int quantity) {
        boolean available;
        try {
            available = Boolean.TRUE.equals(inventoryClient.checkStock(productId, quantity));
        } catch (Exception e) {
            // Inventory Service unreachable - fail safe rather than silently confirming
            Order failedOrder = new Order(productId, quantity, "INVENTORY_SERVICE_UNAVAILABLE");
            return orderRepository.save(failedOrder);
        }

        if (!available) {
            Order rejected = new Order(productId, quantity, "OUT_OF_STOCK");
            return orderRepository.save(rejected);
        }

        Boolean decremented = inventoryClient.decrementStock(productId, quantity);
        String status = Boolean.TRUE.equals(decremented) ? "CONFIRMED" : "OUT_OF_STOCK";

        Order order = new Order(productId, quantity, status);
        return orderRepository.save(order);
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
