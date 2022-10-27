package com.example.telematichub; /**
 * @author Vyacheslav Kirillov
 * @create 2022.10.25 20:29
 **/

//import com.example.telematichub.RMQpart.Runner;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
//import com.example.telematichub.RMQpart.Runner;

@Slf4j
public class Server extends Thread
{
    // открываемый порт сервера
    public static final int port   = 50001;
    private String TEMPL_MSG =
            "The client '%d' sent me message :";
    private String TEMPL_CONN =
            "The client '%d' closed the connection";

    private  Socket socket;
    private  int    num;

    public Server() {}

    public void setSocket(int num, Socket socket)
    {
        // Определение значений
        this.num    = num;
        this.socket = socket;

        // Установка daemon-потока
        setDaemon(true);
        /*
         * Определение стандартного приоритета главного потока
         * int java.lang.Thread.NORM_PRIORITY = 5-the default
         *               priority that is assigned to a thread.
         */
        setPriority(NORM_PRIORITY);
        // Старт потока
        start();
    }

    //Runner s=new Runner("localhost",2);

    public void run()
    {
        try {
            // Определяем входной и выходной потоки сокета
            // для обмена данными с клиентом
            InputStream  sin  = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream  dis = new DataInputStream (sin );
            DataOutputStream dos = new DataOutputStream(sout);

            String line = null;

          //  RabbitTemplate rabbitTemplate=new RabbitTemplate();
          //  rabbitTemplate.convertAndSend(TelematicHubApplication.topicExchangeName, "foo.bar.baz", "The great message 5!");
          //  Runner runner = new Runner();
            SenderToRMQ senderToRMQ=new SenderToRMQ();

            while(true) {
                // Ожидание сообщения от клиента
                line = dis.readUTF();
                log.info(
                        String.format(TEMPL_MSG, num) + line);
                log.info("I'm sending it back...");
                // Отсылаем клиенту обратно эту самую
                // строку текста
                dos.writeUTF("Server receive text : " + line);
                //отправляем сообщение в RMQ
                senderToRMQ.send(line);
                // Завершаем передачу данных
                dos.flush();
                log.info("\n");
                if (line.equalsIgnoreCase("quit")) {
                    // завершаем соединение
                    socket.close();
                    System.out.println(
                            String.format(TEMPL_CONN, num));
                    break;
                }
            }
        } catch(Exception e) {
            log.info("Exception : " + e);
        }
    }
    //---------------------------------------------------------

   /* public static void main(String[] ar)
    {
        ServerSocket srvSocket = null;
        try {
            try {
                int i = 0; // Счётчик подключений
                // Подключение сокета к localhost
                InetAddress ia;
                ia = InetAddress.getByName("localhost");
                srvSocket = new ServerSocket(port, 0, ia);

                System.out.println("Server started\n\n");

                while(true) {
                    // ожидание подключения
                    Socket socket = srvSocket.accept();
                    System.err.println("Client accepted");
                    // Стартуем обработку клиента
                    // в отдельном потоке
                    new Server().setSocket(i++, socket);
                }
            } catch(Exception e) {
                System.out.println("Exception : " + e);
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
    }*/
}