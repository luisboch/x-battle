/**
 * ConnectMessage.class
 */
package com.pucpr.game.server.messages;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class DisconnectMessage extends Message {


    public DisconnectMessage(byte protocol, byte[] messageData) {
        super(protocol, messageData);
    }
    
    @Override
    public void parse() {
    }

    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public byte[] build() {
        return new byte[]{};
    }

}
