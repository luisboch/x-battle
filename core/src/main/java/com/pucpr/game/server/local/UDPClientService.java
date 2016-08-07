/**
 * UDPServer.class
 */
package com.pucpr.game.server.local;

import com.pucpr.game.server.ActorControl;
import com.pucpr.game.server.RemoteSevice;
import com.pucpr.game.server.messages.CommandMessage;
import com.pucpr.game.server.messages.ConnectMessage;
import com.pucpr.game.server.messages.DisconnectMessage;
import com.pucpr.game.server.messages.Message;
import com.pucpr.game.server.messages.MessageParser;
import com.pucpr.game.server.messages.StatusMessage;
import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.World;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class UDPClientService {

    private final int UDP_PORT = 9998;
    private final int UDP_SERVER_PORT = 9999;
    private final long synchMs = 3;
    private final long sendPckMs = 20;
    private final RemoteSevice service;
    private ActorControl control;
    private List<ActorObject> actors = new ArrayList<ActorObject>();
    private long lastReceivedMessage;
    private final MessageParser messageParser = new MessageParser();

    private SenderProcessor senderProcessor;
    private ReceiveProcessor messageReceivedThread;

    private boolean connected = false;
    private boolean stopped = false;
    private InetAddress serverAddr;
    private DatagramSocket udpServer;
    private long timeoutms;

    public UDPClientService(RemoteSevice service) {
        this.service = service;

    }

    public void connect(String host) {
        connect(host, 5000);
    }

    public void connect(String host, long timeoutms) {
        this.timeoutms = timeoutms;
        long startConn = System.currentTimeMillis();
        try {

            udpServer = new DatagramSocket(UDP_PORT);
            serverAddr = InetAddress.getByName(host);

            messageReceivedThread = new ReceiveProcessor();
            messageReceivedThread.start();
            senderProcessor = new SenderProcessor();
            senderProcessor.start();

            ConnectMessage connect = new ConnectMessage(control.getActor());
            senderProcessor.messages.offer(connect);

            while (!connected && (System.currentTimeMillis() - startConn < timeoutms)) {
                Thread.sleep(100);
                senderProcessor.messages.offer(connect);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Connected in " + (System.currentTimeMillis() - startConn));
        if (System.currentTimeMillis() - startConn >= timeoutms) {
            throw new IllegalStateException("Connection timedout");
        }
    }

    public void stop() {
        connected = false;
    }

    public List<ActorObject> getActors() {
        return actors;
    }

    @Override
    protected void finalize() throws Throwable {
        stopped = true; // Stop Threads.
        super.finalize();
    }

    public ActorControl connect(ActorObject obj, String serverPath) {
        control = new ActorControl(obj);
        connect(serverPath);
        return control;
    }

    private class ReceiveProcessor extends Thread {

        private byte[] buffer = new byte[400];
        private MessageParser messageParser = new MessageParser();

        @Override
        public void run() {
            try {

                Thread.sleep(synchMs);

                System.out.printf("Listening on udp:%s:%d%n",
                        InetAddress.getLocalHost().getHostAddress(), udpServer.getPort());
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                byte loopQty = 0;
                long loopAvgSum = 0;

                while (!stopped) {

                    long mls = System.currentTimeMillis();

                    udpServer.receive(receivePacket);
                    lastReceivedMessage = System.currentTimeMillis();
                    messageParser.setFullMsg(buffer);

                    final Message parse = messageParser.parse();
                    if (parse != null) {
                        load(parse);
                    } else {
                        System.out.println("Packet ignored (invalid) ");
                    }

                    loopQty++;
                    loopAvgSum += (System.currentTimeMillis() - mls);

                    if (loopQty == 10) {
                        loopAvgSum /= loopQty;
                        loopQty = 0;
                        System.out.println("Loop: " + loopAvgSum);
                        loopAvgSum = 0;
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

        private void load(Message parse) {
            lastReceivedMessage = System.currentTimeMillis();

            if (parse instanceof StatusMessage) {
                actors = new ArrayList<ActorObject>();
                StatusMessage status = (StatusMessage) parse;
                actors.addAll(status.getObjects());
                control.getActor().setPosition(status.getCurrentPlayer().getPosition());
                control.getActor().setDirection(status.getCurrentPlayer().getDirection());
                connected = true;
            } else if (parse instanceof DisconnectMessage) {
                connected = false;
            }

        }

    }

    private class SenderProcessor extends Thread {

        final Queue<Message> messages = new ConcurrentLinkedQueue<Message>();

        @Override
        public void run() {

            try {

                Thread.sleep(synchMs);

                while (!stopped) {

                    long start = System.currentTimeMillis();

                    Message pool = null;
                    while ((pool = messages.peek()) != null && !stopped) {
                        byte[] sendData = messageParser.build(pool);
                        DatagramPacket sendPacket = new DatagramPacket(
                                sendData, sendData.length, serverAddr, UDP_SERVER_PORT);
                        udpServer.send(sendPacket);
                        messages.remove(pool);
                    }

                    
                    long synchTime = System.currentTimeMillis() - start;
                    long waitTime = sendPckMs - synchTime;

                    if (waitTime > 0) {

                        System.out.println("Waiting for " + waitTime);
                        Thread.sleep(waitTime);
                    }

                    // All times add command msg
                    CommandMessage commandMessage = new CommandMessage(control);
                    messages.offer(commandMessage);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

    }
}
