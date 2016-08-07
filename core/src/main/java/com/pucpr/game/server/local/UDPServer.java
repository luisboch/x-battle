/**
 * UDPServer.class
 */
package com.pucpr.game.server.local;

import com.pucpr.game.server.LocalhostService;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class UDPServer {

    private final LocalhostService service;

    public UDPServer(LocalhostService service) {
        this.service = service;
    }
//
//    private void receive() {
//        try {
//            DatagramSocket serverSocket = new DatagramSocket(9999);
//            
//            System.out.printf("Listening on udp:%s:%d%n",
//                    InetAddress.getLocalHost().getHostAddress(), 9999);
//            DatagramPacket receivePacket = new DatagramPacket(new byte[6], 6);
//
//            while (true) {
//                serverSocket.receive(receivePacket);
//                
//                System.out.println("RECEIVED: " + sentence);
//                // now send acknowledgement packet back to sender     
//                InetAddress IPAddress = receivePacket.getAddress();
//                String sendString = "polo";
//                byte[] sendData = sendString.getBytes("UTF-8");
//                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
//                        IPAddress, 9998);
//                serverSocket.send(sendPacket);
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//    }

}
