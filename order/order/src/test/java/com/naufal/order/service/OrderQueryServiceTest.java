package com.naufal.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

import com.naufal.order.entity.Order;
import com.naufal.order.query.GetAllOrderQuery;
import com.naufal.order.query.GetOrderByIdQuery;
import com.naufal.order.repository.OrderRepository;
import com.naufal.order.vo.Produk;
import com.naufal.order.vo.ResponseTemplate;

import java.net.URI;

/**
 * Unit Test untuk OrderQueryService (CQRS - Query Side)
 * 
 * Test ini menguji semua operasi baca:
 * - Get All Orders
 * - Get Order By ID (beserta data produk dari service lain)
 */
@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DiscoveryClient discoveryClient;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderQueryService orderQueryService;

    private Order sampleOrder1;
    private Order sampleOrder2;

    @BeforeEach
    void setUp() {
        sampleOrder1 = new Order(1L, 101L, 201L, "2024-10-15", 2, 50000.0);
        sampleOrder2 = new Order(2L, 102L, 202L, "2024-10-16", 3, 75000.0);
    }

    // =====================================================
    // TEST: GET ALL ORDERS
    // =====================================================

    @Test
    @DisplayName("Handle GetAllOrderQuery - berhasil mengambil semua order")
    void handleGetAllOrderQuery_returnsAllOrders() {
        // Arrange
        List<Order> orderList = Arrays.asList(sampleOrder1, sampleOrder2);
        when(orderRepository.findAll()).thenReturn(orderList);

        // Act
        List<Order> result = orderQueryService.handle(new GetAllOrderQuery());

        // Assert
        assertNotNull(result, "Daftar order tidak boleh null");
        assertEquals(2, result.size(), "Harus ada 2 order");
        assertEquals(1L, result.get(0).getId(), "Order pertama ID harus 1");
        assertEquals(2L, result.get(1).getId(), "Order kedua ID harus 2");

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Handle GetAllOrderQuery - daftar kosong jika tidak ada order")
    void handleGetAllOrderQuery_returnsEmptyList_whenNoOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of());

        // Act
        List<Order> result = orderQueryService.handle(new GetAllOrderQuery());

        // Assert
        assertNotNull(result, "Hasil tidak boleh null, harus list kosong");
        assertTrue(result.isEmpty(), "Daftar order harus kosong");
    }

    // =====================================================
    // TEST: GET ORDER BY ID
    // =====================================================

    @Test
    @DisplayName("Handle GetOrderByIdQuery - berhasil mengambil order beserta produk")
    void handleGetOrderByIdQuery_returnsOrderWithProduk() throws Exception {
        // Arrange
        Produk mockProduk = new Produk(101L, "Produk A", "Deskripsi A", 25000.0, 1L);
        ServiceInstance mockInstance = mock(ServiceInstance.class);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder1));
        when(discoveryClient.getInstances("PRODUK")).thenReturn(List.of(mockInstance));
        when(mockInstance.getUri()).thenReturn(URI.create("http://localhost:8082"));
        when(restTemplate.getForObject("http://localhost:8082/api/produk/101", Produk.class))
                .thenReturn(mockProduk);

        // Act
        List<ResponseTemplate> result = orderQueryService.handle(new GetOrderByIdQuery(1L));

        // Assert
        assertNotNull(result, "Result tidak boleh null");
        assertEquals(1, result.size(), "Harus ada 1 response template");
        assertEquals(sampleOrder1, result.get(0).getOrder(), "Order harus sama");
        assertEquals(mockProduk, result.get(0).getProduk(), "Produk harus sama");
    }

    @Test
    @DisplayName("Handle GetOrderByIdQuery - return list kosong jika order tidak ditemukan")
    void handleGetOrderByIdQuery_returnsEmpty_whenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        List<ResponseTemplate> result = orderQueryService.handle(new GetOrderByIdQuery(999L));

        // Assert
        assertNotNull(result, "Result tidak boleh null");
        assertTrue(result.isEmpty(), "List harus kosong jika order tidak ditemukan");

        // Pastikan discovery dan restTemplate tidak pernah dipanggil
        verify(discoveryClient, never()).getInstances(any());
        verify(restTemplate, never()).getForObject(anyString(), any());
    }

    @Test
    @DisplayName("Handle GetOrderByIdQuery - return list kosong jika Produk service tidak tersedia")
    void handleGetOrderByIdQuery_returnsEmpty_whenProdukServiceUnavailable() {
        // Arrange: Order ditemukan tapi Produk service tidak ada instance-nya
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder1));
        when(discoveryClient.getInstances("PRODUK")).thenReturn(List.of());

        // Act
        List<ResponseTemplate> result = orderQueryService.handle(new GetOrderByIdQuery(1L));

        // Assert
        assertNotNull(result, "Result tidak boleh null");
        assertTrue(result.isEmpty(), "List harus kosong jika service PRODUK tidak tersedia");
    }

    // =====================================================
    // TEST: QUERY OBJECTS
    // =====================================================

    @Test
    @DisplayName("GetOrderByIdQuery - constructor dan getter berfungsi benar")
    void getOrderByIdQuery_constructorAndGetter() {
        GetOrderByIdQuery query = new GetOrderByIdQuery(77L);
        assertEquals(77L, query.getId());
    }

    @Test
    @DisplayName("GetOrderByIdQuery - setter berfungsi benar")
    void getOrderByIdQuery_setter() {
        GetOrderByIdQuery query = new GetOrderByIdQuery();
        query.setId(88L);
        assertEquals(88L, query.getId());
    }

    @Test
    @DisplayName("GetAllOrderQuery - dapat dibuat tanpa parameter")
    void getAllOrderQuery_instantiation() {
        GetAllOrderQuery query = new GetAllOrderQuery();
        assertNotNull(query, "GetAllOrderQuery harus bisa dibuat tanpa parameter");
    }
}
