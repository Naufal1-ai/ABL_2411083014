package com.naufal.produser;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurasi RabbitMQ untuk PRODUCER (pengirim pesan).
 *
 * Queue "myQueue" di sini HARUS sama dengan yang ada di service consumer,
 * supaya pesan yang dikirim produser bisa diterima consumer.
 */
@Configuration
public class RabbitMQConfiq {

    public static final String QUEUE_NAME = "myQueue";

    /**
     * Queue "myQueue" dengan durable = true.
     * durable membuat queue tetap ada walaupun broker di-restart.
     */
    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * RabbitAdmin mendeklarasikan queue ke broker saat aplikasi start,
     * supaya queue langsung tersedia sebelum dipakai mengirim pesan.
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
