package com.naufal.email.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naufal.email.config.RabbitMQConfig;
import com.naufal.email.event.OrderEvent;
import com.naufal.email.service.EmailService;

/**
 * OrderEventListener = "pintu masuk" event dari RabbitMQ.
 *
 * Class ini berdiri siaga mendengarkan queue "order.notification.queue".
 * Begitu ada event order, method di bawah otomatis dipanggil.
 */
@Component
public class OrderEventListener {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void terimaOrderEvent(OrderEvent event) {
        System.out.println("[EMAIL SERVICE] Menerima event order: " + event.getOrderId());
        emailService.kirimEmailOrder(event);
    }
}
