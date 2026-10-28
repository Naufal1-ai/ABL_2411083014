package com.naufal.order.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naufal.order.command.CreateOrderCommand;
import com.naufal.order.command.DeleteOrderCommand;
import com.naufal.order.command.UpdateOrderCommand;
import com.naufal.order.config.RabbitMQConfig;
import com.naufal.order.entity.Order;
import com.naufal.order.event.OrderEvent;
import com.naufal.order.repository.OrderRepository;

/**
 * CQRS - Command Service
 * 
 * Service ini HANYA bertanggung jawab untuk operasi yang MENGUBAH data:
 * - Create (membuat order baru)
 * - Update (mengubah order yang ada)
 * - Delete (menghapus order)
 *
 * Setelah CREATE berhasil, service ini juga mengirim "event" ke RabbitMQ
 * agar Email Service bisa mengirim notifikasi ke pelanggan.
 */
@Service
public class OrderCommandService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * RabbitTemplate = alat untuk mengirim pesan ke RabbitMQ.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Handle command untuk membuat order baru.
     * Menerima CreateOrderCommand dan menyimpannya ke database.
     * Setelah tersimpan, kirim event ke RabbitMQ (order.notification.queue).
     */
    public Order handle(CreateOrderCommand command) {
        Order order = new Order();
        order.setProdukId(command.getProdukId());
        order.setPelangganId(command.getPelangganId());
        order.setTglTransaksi(command.getTglTransaksi());
        order.setJumlah(command.getJumlah());
        order.setTotal(command.getTotal());
        Order savedOrder = orderRepository.save(order);

        // Kirim event ke RabbitMQ setelah order berhasil disimpan
        kirimEventOrder(savedOrder);

        return savedOrder;
    }

    /**
     * Mengirim event "order baru dibuat" ke RabbitMQ.
     * Email Service akan menerima event ini lewat queue yang sama.
     */
    private void kirimEventOrder(Order order) {
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getProdukId(),
                order.getPelangganId(),
                order.getJumlah(),
                order.getTotal());

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, event);
        System.out.println("[ORDER] Event dikirim ke RabbitMQ: " + event);
    }

    /**
     * Handle command untuk mengupdate order yang sudah ada.
     * Menerima UpdateOrderCommand, mencari order lama, lalu memperbarui datanya.
     */
    public Order handle(UpdateOrderCommand command) {
        Order existing = orderRepository.findById(command.getId()).orElse(null);
        if (existing != null) {
            existing.setProdukId(command.getProdukId());
            existing.setPelangganId(command.getPelangganId());
            existing.setTglTransaksi(command.getTglTransaksi());
            existing.setJumlah(command.getJumlah());
            existing.setTotal(command.getTotal());
            return orderRepository.save(existing);
        }
        return null;
    }

    /**
     * Handle command untuk menghapus order berdasarkan ID.
     * Menerima DeleteOrderCommand dan menghapus order dari database.
     */
    public void handle(DeleteOrderCommand command) {
        orderRepository.deleteById(command.getId());
    }
}
