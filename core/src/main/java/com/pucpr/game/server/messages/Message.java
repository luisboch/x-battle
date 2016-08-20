/**
 * Message.class
 */
package com.pucpr.game.server.messages;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.Planet;
import com.pucpr.game.states.game.Player;
import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.Explosion;
import com.pucpr.game.states.game.engine.PursuitShot;
import com.pucpr.game.states.game.engine.Shot;
import com.pucpr.game.states.game.engine.SimpleShot;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public abstract class Message {

    public static final int INT_BYTE_SIZE = 2;
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

    public static byte[] shortToByte(short val) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(val).array();
    }


    public static short bytesToShort(byte... intBytes) {
        return ByteBuffer.wrap(intBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    protected byte getType(ActorObject obj) {

        if (obj.getClass().equals(Player.class)) {
            return 1;
        } else if (obj.getClass().equals(SimpleShot.class)) {
            return 2;
        } else if (obj.getClass().equals(PursuitShot.class)) {
            return 3;
        } else if (obj.getClass().equals(Explosion.class)) {
            return 4;
        } else if (obj.getClass().equals(Planet.class)) {
            return 5;
        }

        return 0;
    }

    protected ActorObject getActor(byte type, int posX, int posY, int angle, short uid, byte annimationState) {
        ActorObject act = null;
        if (type == 1) {
            act = new Player();
        } else if (type == 2) {
            act = new SimpleShot();
        } else if (type == 3) {
            act = new PursuitShot();
        } else if (type == 4) {
            act = new Explosion();
        } else if (type == 5) {
            act = new Planet();
        }

        if (act != null) {
            act.setPosition(new Vector2(posX, posY));
            Vector2 vel = new Vector2(0.0001f, 0.0001f).nor();
            vel.rotate(angle - vel.angle());
            act.setVelocity(vel);
            act.setDirection(act.getVelocity().cpy().nor());
            act.setuID(uid);
            act.setAnnimationState(annimationState);
        }

        return act;
    }
}
