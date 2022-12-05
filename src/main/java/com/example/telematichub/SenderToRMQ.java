package com.example.telematichub;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SenderToRMQ {
    private final static String QUEUE_NAME = "spring-boot";
    private final static String EXCHANGE_NAME = "TestData3";

    public static void send(String msg) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel())
        {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            log.info("Sent from hub to RMQ " + msg);
        }
        catch (Exception e){
            log.info("Exception "+e);
        }
    }
}
