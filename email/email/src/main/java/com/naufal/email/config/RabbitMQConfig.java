package com.naufal.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurasi RabbitMQ untuk EMAIL SERVICE.
 *
 * Email Service hanya berperan sebagai CONSUMER (penerima):
 * mendengarkan queue "order.notification.queue" yang dikirim service order.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Nama queue HARUS sama persis dengan RabbitMQConfig di service order.
     */
    public static final String ORDER_QUEUE = "order.notification.queue";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    /**
     * Converter JSON: mengubah pesan JSON dari RabbitMQ menjadi object OrderEvent.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
