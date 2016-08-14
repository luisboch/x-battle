/**
 * ConnectMessage.class
 */
package com.pucpr.game.server.messages;

import com.badlogic.gdx.Gdx;
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
    private short screenLimit;

    public ConnectMessage() {
        super(CURRENT_PROTOCOL, new byte[0]);
        screenLimit = (short) (Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? Gdx.graphics.getWidth() : Gdx.graphics.getHeight());
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
        screenLimit = (short) (Gdx.graphics.getWidth() > Gdx.graphics.getHeight() ? Gdx.graphics.getWidth() : Gdx.graphics.getHeight());
    }

    @Override
    public void parse() {
        if (messageData.length < 3) {
            valid = false;
        }
        type = messageData[0];
        screenLimit = (short) bytesToShort(messageData[1], messageData[2]);
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
        byte[] msg = new byte[3];
        msg[0] = type;
        byte[] screen = shortToByte(screenLimit);
        add(screen, msg, 1);
        return msg;
    }

    public byte getType() {
        return type;
    }

    public ActorObject getActor() {
        return getActor(type, 0, 0, 0, (short) 0, (byte) 0);
    }

    public short getScreenLimit() {
        return screenLimit;
    }

    public void setScreenLimit(short screenLimit) {
        this.screenLimit = screenLimit;
    }

    @Override
    public String toString() {
        return "ConnectMessage{" + "type=" + type + ", valid=" + valid + '}';
    }

}
