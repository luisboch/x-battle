/**
 * Message.class
 */
package com.pucpr.game.server.messages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public abstract class Message {

    protected static final byte CURRENT_PROTOCOL = 1;
    protected byte protocol;
    protected byte[] messageData;

    public Message(byte protocol, byte[] messageData) {
        this.protocol = protocol;
        this.messageData = messageData;
    }

    protected byte buildByte(String bits) {
        return Byte.parseByte(bits, 2);
    }

    /**
     * Build 2 values from
     *
     * @param val
     * @return
     */
    protected byte[] buildBytes(int val) {
        return intToByte(val);
    }

    private String complete(String s, int qty, char toPut) {
        if (s == null) {
            return complete("", qty, toPut);
        }

        for (int i = s.length(); i < qty; i++) {
            s = toPut + s;
        }

        return s;
    }

    protected byte getBit(int pos, byte val) {
        return (byte) ((val >> pos) & 1);
    }

    abstract boolean isValid();

    abstract byte[] build();

    abstract void parse();

    protected static enum MessageType {

        CONNECT(1),
        STATUS(2),
        COMMAND(3),
        DISCONNECT(4);

        private final int key;

        MessageType(int val) {
            this.key = val;
        }

        public static MessageType get(short val) {

            if (val == 1) {
                return CONNECT;
            } else if (val == 2) {
                return STATUS;
            } else if (val == 3) {
                return COMMAND;
            } else if (val == 4) {
                return DISCONNECT;
            }

            return COMMAND;
        }

        public static MessageType get(Class<? extends Message> val) {

            if (val.equals(ConnectMessage.class)) {
                return CONNECT;
            } else if (val.equals(StatusMessage.class)) {
                return STATUS;
            } else if (val.equals(CommandMessage.class)) {
                return COMMAND;
            } else if (val.equals(DisconnectMessage.class)) {
                return DISCONNECT;
            }

            return COMMAND;
        }

        public int getKey() {
            return key;
        }
    }

    public void add(byte[] add, byte[] to, int firstIndex) {
        for (int i = 0; i < add.length; i++) {
            to[firstIndex] = add[i];
            firstIndex++;
        }
    }

    public static byte[] intToByte(Integer val) {
        final ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(val);
        return b.array();
    }

    public static int bytesToInt(byte... intBytes) {
        
        byte b1 = intBytes[0];
        byte b2 = intBytes[1];
        byte b3 = intBytes[2];
        byte b4 = intBytes[3];

        int val = ((0xFF & b1) << 24) | ((0xFF & b2) << 16)
                | ((0xFF & b3) << 8) | (0xFF & b4);

        return val;
    }
}
