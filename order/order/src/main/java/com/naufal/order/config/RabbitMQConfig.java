package com.naufal.order.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurasi RabbitMQ untuk service ORDER.
 *
 * Order berperan sebagai PRODUCER: mengirim "event" (kabar) ke RabbitMQ
 * setiap kali ada order baru dibuat. Event ini nanti diterima oleh
 * Email Service (consumer) untuk mengirim notifikasi.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Nama queue notifikasi order.
     * Email service (consumer) HARUS mendengarkan nama queue yang sama.
     */
    public static final String ORDER_QUEUE = "order.notification.queue";

    /**
     * Mendeklarasikan queue ke RabbitMQ.
     * durable = true -> queue tetap ada walau broker di-restart.
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    /**
     * RabbitAdmin memastikan queue dibuat saat aplikasi start.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    /**
     * MessageConverter mengubah object Java <-> JSON.
     * Dengan ini, kita bisa mengirim object OrderEvent langsung,
     * dan RabbitMQ akan menyimpannya sebagai JSON yang rapi.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
