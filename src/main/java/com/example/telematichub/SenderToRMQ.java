package com.example.telematichub;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Vyacheslav Kirillov
 * @create 2022.10.27 18:59
 **/
@Slf4j
@Component
public class SenderToRMQ {
    private final static String QUEUE_NAME = "spring-boot";
    private final static String EXCHANGE_NAME = "TestData3";

    public static void send(String msg) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        log.info("factory ok");
        factory.setHost("localhost");
        log.info("setHost ok");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel())
        {
            log.info("create channel ok");
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            log.info("exchangeDeclare ok");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            log.info("queueDeclare ok");
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            log.info("queueBind ok");
           // String message = "Hello World!";
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            log.info("Sent from hub to RMQ " + msg);
        }
        /*catch (Exception e){
            log.info("Exception "+e);
        }*/
    }
}
