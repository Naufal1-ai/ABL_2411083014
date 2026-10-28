package com.naufal.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naufal.order.entity.Order;
import com.naufal.order.query.GetAllOrderQuery;
import com.naufal.order.query.GetOrderByIdQuery;
import com.naufal.order.service.OrderQueryService;
import com.naufal.order.vo.ResponseTemplate;

/**
 * CQRS - Query Controller
 * 
 * Controller ini HANYA menangani HTTP request yang MEMBACA data:
 * - GET /api/order       → Mengambil semua order
 * - GET /api/order/{id}  → Mengambil order beserta detail produk berdasarkan ID
 */
@RestController
@RequestMapping("/api/order")
public class OrderQueryController {

    @Autowired
    private OrderQueryService orderQueryService;

    /**
     * GET /api/order
     * Mengambil daftar semua order dari database.
     */
    @GetMapping
    public List<Order> getAllOrder() {
        return orderQueryService.handle(new GetAllOrderQuery());
    }

    /**
     * GET /api/order/{id}
     * Mengambil detail order beserta data produk berdasarkan ID order.
     */
    @GetMapping("/{id}")
    public List<ResponseTemplate> getOrderById(@PathVariable("id") Long id) {
        return orderQueryService.handle(new GetOrderByIdQuery(id));
    }
}
