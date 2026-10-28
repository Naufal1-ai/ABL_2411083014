package com.naufal.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.naufal.order.entity.Order;
import com.naufal.order.query.GetAllOrderQuery;
import com.naufal.order.query.GetOrderByIdQuery;
import com.naufal.order.repository.OrderRepository;
import com.naufal.order.vo.Produk;
import com.naufal.order.vo.ResponseTemplate;

/**
 * CQRS - Query Service
 * 
 * Service ini HANYA bertanggung jawab untuk operasi yang MEMBACA data:
 * - GetAll (mengambil semua order)
 * - GetById (mengambil order beserta detail produk berdasarkan ID)
 */
@Service
public class OrderQueryService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Handle query untuk mengambil semua order dari database.
     */
    public List<Order> handle(GetAllOrderQuery query) {
        return orderRepository.findAll();
    }

    /**
     * Handle query untuk mengambil order berdasarkan ID beserta data produknya.
     * Melakukan komunikasi antar service ke PRODUK service via Eureka Discovery.
     */
    public List<ResponseTemplate> handle(GetOrderByIdQuery query) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Order order = orderRepository.findById(query.getId()).orElse(null);

        if (order != null) {
            List<ServiceInstance> instances = discoveryClient.getInstances("PRODUK");
            if (instances != null && !instances.isEmpty()) {
                ServiceInstance serviceInstance = instances.get(0);
                Produk produk = restTemplate.getForObject(
                        serviceInstance.getUri() + "/api/produk/" + order.getProdukId(),
                        Produk.class);
                ResponseTemplate vo = new ResponseTemplate();
                vo.setOrder(order);
                vo.setProduk(produk);
                responseList.add(vo);
            }
        }

        return responseList;
    }
}
