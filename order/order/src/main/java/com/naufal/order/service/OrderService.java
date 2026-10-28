package com.naufal.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.naufal.order.entity.Order;
import com.naufal.order.repository.OrderRepository;
import com.naufal.order.vo.Produk;
import com.naufal.order.vo.ResponseTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        Order existing = orderRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setProdukId(order.getProdukId());
            existing.setPelangganId(order.getPelangganId());
            existing.setTglTransaksi(order.getTglTransaksi());
            existing.setJumlah(order.getJumlah());
            existing.setTotal(order.getTotal());
            return orderRepository.save(existing);
        }
        return null;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<ResponseTemplate> getAllOrderWithProdukById(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Order order = getOrderById(id);
        if (order != null) {
            List<ServiceInstance> instances = discoveryClient.getInstances("PRODUK");
            if (instances != null && !instances.isEmpty()) {
                ServiceInstance serviceInstance = instances.get(0);
                Produk produk = restTemplate.getForObject(serviceInstance.getUri() + "/api/produk/" + order.getProdukId(), Produk.class);
                ResponseTemplate vo = new ResponseTemplate();
                vo.setOrder(order);
                vo.setProduk(produk);
                responseList.add(vo);
            }
        }
        return responseList;
    }
}
