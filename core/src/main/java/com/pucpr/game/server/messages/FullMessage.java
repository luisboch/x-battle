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
public class FullMessage {

    private byte protocol;
    private byte messageType;
    private byte[] messageData;

    public FullMessage() {
    }

    public Message parse(byte[] fullMsg) {

        if (fullMsg == null || fullMsg.length == 0) {
            return null;
        }

        protocol = fullMsg[0];
        messageType = fullMsg[1];

        if (fullMsg.length > 2) {
            messageData = sub(2, 4, fullMsg);
        }

        int size = Message.bytesToInt(new byte[]{
            fullMsg[fullMsg.length - 4],
            fullMsg[fullMsg.length - 3],
            fullMsg[fullMsg.length - 2],
            fullMsg[fullMsg.length - 1]});

        if (size == fullMsg.length) {
            Message message = getMessage(messageType, messageData);
            message.parse();
            return message;
        }

        return null;

    }

    public byte[] build(Message message) {

        protocol = Message.CURRENT_PROTOCOL;
        messageType = (byte) Message.MessageType.get((Class<Message>) message.getClass()).getKey();
        messageData = message.build();

        byte[] result = new byte[6 + messageData.length];// Allocate protocol, type, and, at end, the lenght.;
        result[0] = protocol;
        result[1] = messageType;

        int currentIdx = 2;
        for (int i = 0; i < messageData.length; i++) {

            result[currentIdx] = messageData[i];
            currentIdx++;
        }

        byte[] messageLength = Message.intToByte(result.length);

        for (int i = 0; i < messageLength.length; i++) {
            result[currentIdx] = messageLength[i];
            currentIdx++;
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
