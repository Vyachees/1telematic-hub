package com.example.telematichub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.telematichub.Server.port;

@SpringBootApplication
@Slf4j
public class TelematicHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelematicHubApplication.class, args);
        ServerSocket srvSocket = null;
        log.info("MyClassPath is "+System.getProperty("java.class.path"));
        try {
            try {
                int i = 0; // Count connects
                // Connect to localhost
                InetAddress ia;
                ia = InetAddress.getByName("localhost");
                srvSocket = new ServerSocket(port, 0, ia);

                System.out.println("Server started\n\n");

                while(true) {
                    Socket socket = srvSocket.accept();
                    System.err.println("Client accepted");
                    // Start in separate thread
                    new Server().setSocket(i++, socket);
                }
            } catch(Exception e) {
                log.info("Exception : " + e);
            }
        } finally {
            try {
                if (srvSocket != null)
                    srvSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}


