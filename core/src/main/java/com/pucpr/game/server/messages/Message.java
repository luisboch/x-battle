/**
 * Message.class
 */
package com.pucpr.game.server.messages;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.states.game.Nave1;
import com.pucpr.game.states.game.Nave2;
import com.pucpr.game.states.game.Nave3;
import com.pucpr.game.states.game.Planet;
import com.pucpr.game.states.game.Player;
import com.pucpr.game.states.game.engine.ActorObject;
import com.pucpr.game.states.game.engine.Explosion;
import com.pucpr.game.states.game.engine.MineShot;
import com.pucpr.game.states.game.engine.PursuitShot;
import com.pucpr.game.states.game.engine.SimpleShot;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

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

    public void add(byte[] add, List<Object> to, int firstIndex) {
        for (int i = 0; i < add.length; i++) {
            to.add(firstIndex++, add[i]);
        }
    }

    public static byte[] shortToByte(short val) {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(val).array();
    }

    public static short bytesToShort(byte... intBytes) {
        return ByteBuffer.wrap(intBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    protected byte getType(ActorObject obj) {

        if (obj.getClass().equals(Nave1.class)) {
            return 1;
        } else if (obj.getClass().equals(Nave2.class)) {
            return 2;
        } else if (obj.getClass().equals(Nave3.class)) {
            return 3;
        } else if (obj.getClass().equals(PursuitShot.class)) {
            return 4;
        } else if (obj.getClass().equals(SimpleShot.class)) {
            return 5;
        } else if (obj.getClass().equals(Explosion.class)) {
            return 6;
        } else if (obj.getClass().equals(Planet.class)) {
            return 7;
        } else if (obj.getClass().equals(MineShot.class)) {
            return 8;
        }

        return 0;
    }

    protected Class getType(byte obj) {

        switch (obj) {
            case 1:
                return Nave1.class;
            case 2:
                return Nave2.class;
            case 3:
                return Nave3.class;
            case 4:
                return PursuitShot.class;
            case 5:
                return SimpleShot.class;
            case 6:
                return Explosion.class;
            case 7:
                return Planet.class;
            case 8:
                return MineShot.class;
            default:
                break;
        }

        return null;
    }

    protected ActorObject getActor(byte type, int posX, int posY, int angle, short uid, byte annimationState, byte health) {
        return getActor(type, posX, posY, angle, uid, annimationState, health, -1, -1, -1,
                -1, -1, -1, (byte) 1, (byte) 1, (byte) 1);
    }

    protected ActorObject getActor(
            byte type, int posX, int posY, int angle, short uid,
            byte annimationState, byte health,
            int sh1posX, int sh1posY,
            int sh2posX, int sh2posY,
            int sh3posX, int sh3posY,
            byte sh1Health, byte sh2Health, byte sh3Health) {
        try {
            final Class<ActorObject> clazz = getType(type);
            final ActorObject act = clazz.newInstance();

            if (act != null) {
                act.setPosition(new Vector2(posX, posY));
                Vector2 vel = new Vector2(0.0001f, 0.0001f).nor();
                vel.rotate(angle - vel.angle());
                act.setVelocity(vel);
                act.setDirection(act.getVelocity().cpy().nor());
                act.setuID(uid);
                act.setAnnimationState(annimationState);

                if (act instanceof Player) {
                    final List<ActorObject> shields = act.getChildren();

                    final ActorObject sh1 = shields.get(0);
                    final ActorObject sh2 = shields.get(1);
                    final ActorObject sh3 = shields.get(2);

                    sh1.setPosition(new Vector2(sh1posX, sh1posY));
                    sh1.setHealthByPercent(sh1Health);
                    sh2.setPosition(new Vector2(sh2posX, sh2posY));
                    sh2.setHealthByPercent(sh2Health);
                    sh3.setPosition(new Vector2(sh3posX, sh3posY));
                    sh3.setHealthByPercent(sh3Health);
                }
            }

            return act;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw new RuntimeException(ex);
        }
    }
}
