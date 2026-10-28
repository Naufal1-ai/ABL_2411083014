package com.naufal.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurasi RabbitMQ untuk CONSUMER (latihan dasar).
 *
 * Consumer ini hanya mendengarkan queue "myQueue" berisi pesan teks biasa.
 * Untuk fitur email, lihat service terpisah: "email".
 */
@Configuration
public class RabbitMQConfiq {

    public static final String QUEUE_NAME = "myQueue";

    @Bean
    public Queue myQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
