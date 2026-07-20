package com.akhil.order.controller;

import com.akhil.order.model.Order;
import com.akhil.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order placeOrder(@RequestParam Long productId, @RequestParam int quantity) {
        return orderService.placeOrder(productId, quantity);
    }

    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
