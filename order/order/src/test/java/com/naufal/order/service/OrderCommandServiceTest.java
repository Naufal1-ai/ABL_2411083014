package com.naufal.order.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.naufal.order.command.CreateOrderCommand;
import com.naufal.order.command.DeleteOrderCommand;
import com.naufal.order.command.UpdateOrderCommand;
import com.naufal.order.entity.Order;
import com.naufal.order.repository.OrderRepository;

/**
 * Unit Test untuk OrderCommandService (CQRS - Command Side)
 * 
 * Test ini menguji semua operasi tulis:
 * - Create Order
 * - Update Order
 * - Delete Order
 */
@ExtendWith(MockitoExtension.class)
class OrderCommandServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderCommandService orderCommandService;

    private Order sampleOrder;
    private CreateOrderCommand createCommand;

    @BeforeEach
    void setUp() {
        // Data sample order untuk dipakai di semua test
        sampleOrder = new Order(1L, 101L, 201L, "2024-10-15", 2, 50000.0);

        // Command untuk membuat order baru
        createCommand = new CreateOrderCommand(101L, 201L, "2024-10-15", 2, 50000.0);
    }

    // =====================================================
    // TEST: CREATE ORDER
    // =====================================================

    @Test
    @DisplayName("Handle CreateOrderCommand - berhasil membuat order baru")
    void handleCreateOrderCommand_success() {
        // Arrange: Siapkan mock - ketika repository.save() dipanggil, kembalikan sampleOrder
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        // Act: Jalankan command
        Order result = orderCommandService.handle(createCommand);

        // Assert: Pastikan hasilnya benar
        assertNotNull(result, "Order yang disimpan tidak boleh null");
        assertEquals(1L, result.getId(), "ID order harus 1");
        assertEquals(101L, result.getProdukId(), "Produk ID harus 101");
        assertEquals(201L, result.getPelangganId(), "Pelanggan ID harus 201");
        assertEquals("2024-10-15", result.getTglTransaksi(), "Tanggal transaksi harus sesuai");
        assertEquals(2, result.getJumlah(), "Jumlah harus 2");
        assertEquals(50000.0, result.getTotal(), "Total harus 50000");

        // Pastikan repository.save() dipanggil tepat 1 kali
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Handle CreateOrderCommand - repository dipanggil dengan data yang benar")
    void handleCreateOrderCommand_repositoryCalledWithCorrectData() {
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        orderCommandService.handle(createCommand);

        // Verifikasi bahwa save() dipanggil dengan Order yang memiliki data dari command
        verify(orderRepository).save(argThat(order ->
            order.getProdukId().equals(101L) &&
            order.getPelangganId().equals(201L) &&
            order.getTglTransaksi().equals("2024-10-15") &&
            order.getJumlah().equals(2) &&
            order.getTotal().equals(50000.0)
        ));
    }

    // =====================================================
    // TEST: UPDATE ORDER
    // =====================================================

    @Test
    @DisplayName("Handle UpdateOrderCommand - berhasil update order yang ada")
    void handleUpdateOrderCommand_success() {
        // Arrange
        UpdateOrderCommand updateCommand = new UpdateOrderCommand(
            1L, 102L, 202L, "2024-11-01", 5, 75000.0);
        Order updatedOrder = new Order(1L, 102L, 202L, "2024-11-01", 5, 75000.0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        Order result = orderCommandService.handle(updateCommand);

        // Assert
        assertNotNull(result, "Order hasil update tidak boleh null");
        assertEquals(102L, result.getProdukId(), "Produk ID harus terupdate menjadi 102");
        assertEquals(202L, result.getPelangganId(), "Pelanggan ID harus terupdate menjadi 202");
        assertEquals("2024-11-01", result.getTglTransaksi(), "Tanggal harus terupdate");
        assertEquals(5, result.getJumlah(), "Jumlah harus terupdate menjadi 5");
        assertEquals(75000.0, result.getTotal(), "Total harus terupdate menjadi 75000");

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Handle UpdateOrderCommand - return null jika order tidak ditemukan")
    void handleUpdateOrderCommand_orderNotFound_returnsNull() {
        // Arrange: Order tidak ditemukan di database
        UpdateOrderCommand updateCommand = new UpdateOrderCommand(
            999L, 102L, 202L, "2024-11-01", 5, 75000.0);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Order result = orderCommandService.handle(updateCommand);

        // Assert: Harus return null
        assertNull(result, "Harus return null jika order tidak ditemukan");

        // Pastikan save() tidak pernah dipanggil
        verify(orderRepository, never()).save(any(Order.class));
    }

    // =====================================================
    // TEST: DELETE ORDER
    // =====================================================

    @Test
    @DisplayName("Handle DeleteOrderCommand - berhasil menghapus order")
    void handleDeleteOrderCommand_success() {
        // Arrange
        DeleteOrderCommand deleteCommand = new DeleteOrderCommand(1L);
        doNothing().when(orderRepository).deleteById(1L);

        // Act
        orderCommandService.handle(deleteCommand);

        // Assert: Pastikan deleteById() dipanggil dengan ID yang benar
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Handle DeleteOrderCommand - repository.deleteById dipanggil dengan ID benar")
    void handleDeleteOrderCommand_correctIdPassed() {
        // Arrange
        Long targetId = 42L;
        DeleteOrderCommand deleteCommand = new DeleteOrderCommand(targetId);
        doNothing().when(orderRepository).deleteById(targetId);

        // Act
        orderCommandService.handle(deleteCommand);

        // Assert
        verify(orderRepository).deleteById(targetId);
        verify(orderRepository, never()).deleteById(argThat(id -> !id.equals(targetId)));
    }

    // =====================================================
    // TEST: COMMAND OBJECTS
    // =====================================================

    @Test
    @DisplayName("CreateOrderCommand - constructor dan getter berfungsi benar")
    void createOrderCommand_constructorAndGetters() {
        CreateOrderCommand cmd = new CreateOrderCommand(10L, 20L, "2024-01-01", 3, 30000.0);

        assertEquals(10L, cmd.getProdukId());
        assertEquals(20L, cmd.getPelangganId());
        assertEquals("2024-01-01", cmd.getTglTransaksi());
        assertEquals(3, cmd.getJumlah());
        assertEquals(30000.0, cmd.getTotal());
    }

    @Test
    @DisplayName("UpdateOrderCommand - setter berfungsi benar")
    void updateOrderCommand_setters() {
        UpdateOrderCommand cmd = new UpdateOrderCommand();
        cmd.setId(5L);
        cmd.setProdukId(50L);
        cmd.setPelangganId(60L);
        cmd.setTglTransaksi("2024-06-01");
        cmd.setJumlah(10);
        cmd.setTotal(100000.0);

        assertEquals(5L, cmd.getId());
        assertEquals(50L, cmd.getProdukId());
        assertEquals(60L, cmd.getPelangganId());
        assertEquals("2024-06-01", cmd.getTglTransaksi());
        assertEquals(10, cmd.getJumlah());
        assertEquals(100000.0, cmd.getTotal());
    }

    @Test
    @DisplayName("DeleteOrderCommand - constructor dan getter berfungsi benar")
    void deleteOrderCommand_constructorAndGetter() {
        DeleteOrderCommand cmd = new DeleteOrderCommand(99L);
        assertEquals(99L, cmd.getId());
    }
}
