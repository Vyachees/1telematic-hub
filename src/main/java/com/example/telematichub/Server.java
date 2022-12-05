package com.example.telematichub;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.Socket;

@Slf4j
public class Server extends Thread
{
    //Opening the server port
    public static final int port   = 50001;

    private  Socket socket;
    private  int    num;

    public Server() {}

    public void setSocket(int num, Socket socket)
    {
        // Definition of values
        this.num    = num;
        this.socket = socket;

        // Installing a daemon thread
        setDaemon(true);
        /*
         * Determining the default priority of the main thread
         * int java.lang.Thread.NORM_PRIORITY = 5-the default
         *               priority that is assigned to a thread.
         */
        setPriority(NORM_PRIORITY);
        start();
    }



    public void run()
    {
        try {
            // In and out streams
            InputStream  sin  = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();
            DataInputStream  dis = new DataInputStream (sin );
            DataOutputStream dos = new DataOutputStream(sout);
            String line;

            while(true) {
                // Waiting for a message from the client
                line = dis.readUTF();
                String TEMPL_MSG = "The client '%d' sent me message :";
                log.info(
                        String.format(TEMPL_MSG, num) + line);
                log.info("I'm sending it back...");
                // We send back to the client this same line of text
                dos.writeUTF("Server receive text : " + line);
                //Send message to RMQ
                SenderToRMQ.send(line);
                // We complete the data transfer
                dos.flush();
                log.info("\n");
                if (line.equalsIgnoreCase("quit")) {
                    // End the connection
                    socket.close();
                    String TEMPL_CONN = "The client '%d' closed the connection";
                    log.info(String.format(TEMPL_CONN, num));
                    break;
                }
            }
        } catch(Exception e) {
            log.info("Exception : " + e);
        }
    }
}
