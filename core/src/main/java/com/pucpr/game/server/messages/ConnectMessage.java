/**
 * ConnectMessage.class
 */
package com.pucpr.game.server.messages;

import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class ConnectMessage extends Message {

    private byte type;
    private boolean valid;

    public ConnectMessage() {
        super(CURRENT_PROTOCOL, new byte[0]);
    }
    public ConnectMessage(byte type) {
        this();
        this.type = type;
    }
    public ConnectMessage(ActorObject type) {
        this();
        this.type = getType(type);
    }

    public ConnectMessage(byte protocol, byte[] messageData) {
        super(protocol, messageData);
    }

    @Override
    public void parse() {
        if (messageData.length < 1) {
            valid = false;
        }
        type = messageData[0];
        valid = type != 0;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Override
    public byte[] build() {
        return new byte[]{type};
    }

    public byte getType() {
        return type;
    }
    
    public ActorObject getActor(){
        return getActor(type, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "ConnectMessage{" + "type=" + type + ", valid=" + valid + '}';
    }
}
