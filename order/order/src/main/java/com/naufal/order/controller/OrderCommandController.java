package com.naufal.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naufal.order.command.CreateOrderCommand;
import com.naufal.order.command.DeleteOrderCommand;
import com.naufal.order.command.UpdateOrderCommand;
import com.naufal.order.entity.Order;
import com.naufal.order.service.OrderCommandService;

/**
 * CQRS - Command Controller
 * 
 * Controller ini HANYA menangani HTTP request yang MENGUBAH data:
 * - POST   /api/order        → Membuat order baru
 * - PUT    /api/order/{id}   → Mengupdate order
 * - DELETE /api/order/{id}   → Menghapus order
 */
@RestController
@RequestMapping("/api/order")
public class OrderCommandController {

    @Autowired
    private OrderCommandService orderCommandService;

    /**
     * POST /api/order
     * Membuat order baru dari data yang dikirim sebagai CreateOrderCommand.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderCommand command) {
        Order savedOrder = orderCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    /**
     * PUT /api/order/{id}
     * Mengupdate order yang ada berdasarkan ID dari path variable.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @RequestBody UpdateOrderCommand command) {
        command.setId(id); // Set ID dari path variable ke dalam command
        Order updated = orderCommandService.handle(command);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/order/{id}
     * Menghapus order berdasarkan ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderCommandService.handle(new DeleteOrderCommand(id));
        return ResponseEntity.noContent().build();
    }
}
