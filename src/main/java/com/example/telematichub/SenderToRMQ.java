package com.example.telematichub;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vyacheslav Kirillov
 * @create 2022.10.27 18:59
 **/
@Slf4j
public class SenderToRMQ {
    private final static String QUEUE_NAME = "spring-boot";
    public static void send(String msg) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
           // String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            log.info("Sent from hub to RMQ " + msg);
        }
    }
}
