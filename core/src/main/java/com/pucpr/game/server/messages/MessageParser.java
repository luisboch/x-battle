/**
 * Message.class
 */
package com.pucpr.game.server.messages;

import java.nio.ByteBuffer;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class MessageParser {

    private byte protocol;
    private byte messageType;
    private byte[] messageData;
    private byte[] fullMsg;

    public MessageParser() {
    }

    public void setFullMsg(byte[] fullMsg) {
        this.fullMsg = fullMsg;
    }

    public Message parse() {

        if (fullMsg == null || fullMsg.length == 0) {
            return null;
        }

        int idx = 0;
        int size = Message.bytesToInt(new byte[]{fullMsg[idx++], fullMsg[idx++],
            fullMsg[idx++], fullMsg[idx++]});

        if (size <= fullMsg.length) {
            protocol = fullMsg[idx++];
            messageType = fullMsg[idx++];

            if (fullMsg.length > 6) {
                byte[] fixedMsg = new byte[size];
                cpy(fullMsg, fixedMsg);

                messageData = sub(6, 0, fixedMsg);
            }

            Message message = getMessage(messageType, messageData);
            message.parse();
            
            return message.isValid() ? message : null;
        }

        return null;

    }

    public byte[] build(Message message) {

        protocol = Message.CURRENT_PROTOCOL;
        messageType = (byte) Message.MessageType.get((Class<Message>) message.getClass()).getKey();
        messageData = message.build();

        byte[] result = new byte[6 + messageData.length];// Allocate protocol, type, and, at end, the lenght.;
        byte[] messageLength = Message.intToByte(result.length);

        int idx = 0;
        for (int i = 0; i < messageLength.length; i++) {
            result[idx++] = messageLength[i];
        }

        result[idx++] = protocol;
        result[idx++] = messageType;

        for (int i = 0; i < messageData.length; i++) {

            result[idx++] = messageData[i];
        }

        return result;
    }

    private byte[] sub(int beginSub, int endSub, byte[] message) {

        byte[] rs = new byte[message.length - beginSub - endSub];

        for (int i = beginSub; i < message.length - endSub; i++) {
            rs[i - beginSub] = message[i];
        }

        return rs;
    }

    private void cpy(byte[] bufferFrom, byte[] bufferTo) {
        for (int i = 0; i < bufferTo.length; i++) {
            bufferTo[i] = bufferFrom[i];
        }
    }

    private Message getMessage(byte messageType, byte[] messageData) {

        if (((int) messageType) == Message.MessageType.CONNECT.getKey()) {
            return new ConnectMessage(protocol, messageData);
        } else if (((int) messageType) == Message.MessageType.COMMAND.getKey()) {
            return new CommandMessage(protocol, messageData);
        } else if (((int) messageType) == Message.MessageType.STATUS.getKey()) {
            return new StatusMessage(protocol, messageData);
        } else if (((int) messageType) == Message.MessageType.DISCONNECT.getKey()) {
            return new DisconnectMessage(protocol, messageData);
        }

        throw new IllegalArgumentException("The message type is unknow!");
    }

}
