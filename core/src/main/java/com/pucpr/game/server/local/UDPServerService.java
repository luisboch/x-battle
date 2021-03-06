/**
 * UDPServer.class
 */
package com.pucpr.game.server.local;

import com.pucpr.game.server.ActorControl;
import com.pucpr.game.server.LocalhostService;
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
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class UDPServerService {

    private final int UDP_CLIENT_PORT = 9998;
    private final int UDP_SERVER_PORT = 9999;
    private final long synchMs = 3;
    private final long sendPckMs = 20;
    private final LocalhostService service;

    private Map<InetAddress, ClientInfo> clients = new ConcurrentHashMap<InetAddress, ClientInfo>();

    private ActorControl control;
    private final World world;
    private final MessageParser messageParser = new MessageParser();

    private SenderProcessor senderProcessor;
    private ReceiveProcessor messageReceivedThread;

    private boolean stopped = false;
    private DatagramSocket udpServer;
    private long timeoutms;
    private boolean connected = false;

    public UDPServerService(World world, LocalhostService service) {
        this.service = service;
        this.world = world;

    }

    public void connect() {
        connect(200);
    }

    public void connect(long timeoutms) {
        this.timeoutms = timeoutms;
        long startConn = System.currentTimeMillis();
        try {

            udpServer = new DatagramSocket(UDP_SERVER_PORT);

            messageReceivedThread = new ReceiveProcessor();
            messageReceivedThread.start();
            senderProcessor = new SenderProcessor();
            senderProcessor.start();

            connected = true;

        } catch (Exception e) {
            System.out.println(e);
        }

        if (System.currentTimeMillis() - startConn >= timeoutms) {
            throw new IllegalStateException("Connection timedout");
        }
    }

    public void stop() {

        for (ClientInfo inf : clients.values()) {
            senderProcessor.messages.add(new ClientMesg(inf.addr, new DisconnectMessage()));
        }

        while (!senderProcessor.messages.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }

        clients.clear();
        stopped = true;
    }

    @Override
    protected void finalize() throws Throwable {
        stopped = true; // Stop Threads.
        super.finalize();
    }

    public ActorControl connect(ActorObject obj) {
        control = world.create(obj);

        if (!connected) {
            connect();
        }

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

                while (!stopped) {
                    udpServer.receive(receivePacket);
                    messageParser.setFullMsg(buffer);

                    final Message parse = messageParser.parse();
                    if (parse != null) {
                        load(receivePacket.getAddress(), parse, messageParser.getMessageSeq());
                    } else {
                        System.out.println("Packet ignored (invalid) ");
                    }

                    Thread.sleep(synchMs);
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

        private void load(InetAddress address, Message msg, short msgSeq) {
            ClientInfo info = null;
            if (msg instanceof ConnectMessage) {

                final ConnectMessage connect = (ConnectMessage) msg;

                if (!clients.containsKey(address)) {
                    info = new ClientInfo();
                    ActorObject actor = ((ConnectMessage) msg).getActor();

                    info.control = world.create(actor);
                    info.addr = address;
                    info.lastMessageSeq = msgSeq;
                    info.viewSize = connect.getScreenLimit();

                    clients.put(address, info);

                } else {
                    info = clients.get(address);
                }

            } else {

                info = clients.get(address);
                if (info != null && isUpdatedMsg(info, msgSeq)) {
                    if (msg instanceof CommandMessage) {
                        CommandMessage cmd = (CommandMessage) msg;
                        info.control.setUp(cmd.isUP());
                        info.control.setRight(cmd.isRIGHT());
                        info.control.setDown(cmd.isDOWN());
                        info.control.setLeft(cmd.isLEFT());
                        info.control.setForce(cmd.isFORCE());
                        info.control.setAction1(cmd.isACTION1());
                        info.control.setAction2(cmd.isACTION2());
                        info.control.setAction3(cmd.isACTION3());
                        info.control.setMouseX(cmd.getMouseX());
                        info.control.setMouseY(cmd.getMouseY());
                    } else if (msg instanceof DisconnectMessage) {
                        clients.remove(address);
                        senderProcessor.messages.add(new ClientMesg(address, new DisconnectMessage()));
                    }
                }
            }

            if (info != null) {
                info.lastReceivedMessage = System.currentTimeMillis();
            }
        }

        private boolean isUpdatedMsg(ClientInfo info, short msgSeq) {
            boolean rs = false;
            if (info == null) {
                rs = false;
            } else if (info.lastMessageSeq < msgSeq) {
                rs = true;
            } else if (info.lastMessageSeq > 31000 && msgSeq < 1000) {
                rs = true;
            }

            if (rs) {
                info.lastMessageSeq = msgSeq;
            }

            return rs;
        }

    }

    private class SenderProcessor extends Thread {

        final Queue<ClientMesg> messages = new ConcurrentLinkedQueue<ClientMesg>();

        @Override
        public void run() {

            try {

                Thread.sleep(50);

                while (!stopped) {
                    long startLoop = System.currentTimeMillis();

                    ClientMesg pool = null;
                    while ((pool = messages.peek()) != null && !stopped) {
                        messageParser.setMessageSeq(getNextMessageSeq(pool.addr));
                        byte[] sendData = messageParser.build(pool.msg);
                        DatagramPacket sendPacket = new DatagramPacket(
                                sendData, sendData.length, pool.addr, UDP_CLIENT_PORT);
                        udpServer.send(sendPacket);
                        messages.remove(pool);
                    }

                    // All times add status msg
                    for (ClientInfo c : clients.values()) {
                        final StatusMessage msg = new StatusMessage();
                        final List<ActorObject> objs = world.getVisibleActors(c.control.getActor().getPosition(), (float) c.viewSize);
                        msg.addAll(objs);
                        msg.setCurrentPlayer(c.control.getActor());
                        messages.offer(new ClientMesg(c.addr, msg));
                    }

                    long timeProcessing = System.currentTimeMillis() - startLoop;

                    if (timeProcessing < sendPckMs) { // Ensure that we wait 50ms on loop (when it reaches ignore stop)
                        long wait = sendPckMs - timeProcessing;
                        Thread.sleep(wait);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

        private short getNextMessageSeq(InetAddress addr) {
            final ClientInfo info = clients.get(addr);
                    
            if (info.messageSeq > 32000) {
                info.messageSeq = 0;
            }

            return info.messageSeq++;
        }
    }

    private static class ClientMesg {

        private InetAddress addr;
        private Message msg;

        public ClientMesg() {
        }

        public ClientMesg(InetAddress addr, Message msg) {
            this.addr = addr;
            this.msg = msg;
        }

    }

    private static class ClientInfo {

        private InetAddress addr;
        private ActorControl control;
        private long lastReceivedMessage;
        private short lastMessageSeq = -1;
        private short messageSeq = 0;
        private short viewSize;
    }
}
