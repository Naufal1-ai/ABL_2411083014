package com.naufal.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * CONSUMER RabbitMQ (latihan dasar)
 *
 * Mendengarkan queue "myQueue" berisi pesan teks biasa.
 * Untuk fitur email, lihat service terpisah: "email".
 */
@Component
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConfiq.QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("========================================");
        System.out.println("PESAN DITERIMA DARI RABBITMQ: " + message);
        System.out.println("========================================");
    }
}
