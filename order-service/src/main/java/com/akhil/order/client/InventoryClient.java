package com.akhil.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// name + url: url points straight at Inventory Service for this demo.
// Swap the hardcoded url for Eureka service discovery (name only) once
// you add a discovery server — that's the natural next step after this.
@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryClient {

    @GetMapping("/api/inventory/products/{id}/check")
    Boolean checkStock(@PathVariable("id") Long productId, @RequestParam("qty") int qty);

    @PostMapping("/api/inventory/products/{id}/decrement")
    Boolean decrementStock(@PathVariable("id") Long productId, @RequestParam("qty") int qty);
}
