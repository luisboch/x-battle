/**
 * StatusMessage.class
 */
package com.pucpr.game.server.messages;

import com.pucpr.game.states.game.engine.ActorObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class StatusMessage extends Message {

    private static final int SIZE_PER_EL = 10;

    private boolean valid;
    private ActorObject currentPlayer;

    private final Map<ActorObject, Byte> objects = new HashMap<ActorObject, Byte>();

    public StatusMessage() {
        super(CURRENT_PROTOCOL, new byte[0]);
    }

    public StatusMessage(byte protocol, byte[] messageData) {
        super(protocol, messageData);
    }

    public StatusMessage add(ActorObject obj) {
        objects.put(obj, getType(obj));
        return this;
    }

    public StatusMessage addAll(List<ActorObject> list) {
        for (ActorObject obj : list) {
            objects.put(obj, getType(obj));
        }

        return this;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public byte[] build() {
        /**
         * byte structure:<br>
         * 0 = type;<br>
         * 1 = first posX;<br>
         * 2 = second posX;<br>
         * 3 = first posY;<br>
         * 4 = second posY;<br>
         * 5 = first angle;<br>
         * 6 = second angle;<br>
         * 7 = first uid;<br>
         * 8 = second uid;<br>
         */
        byte[] data = new byte[objects.size() * SIZE_PER_EL + SIZE_PER_EL]; // we will use 7 bytes for each element + current player

        int idx = 0;

        data[idx] = getType(currentPlayer);
        idx++;
        add(shortToByte((short) currentPlayer.getPosition().x), data, idx);
        idx += INT_BYTE_SIZE;
        add(shortToByte((short) currentPlayer.getPosition().y), data, idx);
        idx += INT_BYTE_SIZE;
        int angle = (int) currentPlayer.getDirection().angle();
        byte[] angleBytes = shortToByte((short) angle);
        byte[] uidb = shortToByte(currentPlayer.getuID());

        add(angleBytes, data, idx);
        idx += INT_BYTE_SIZE;

        add(uidb, data, idx);
        idx += INT_BYTE_SIZE;
        data[idx++] = currentPlayer.getAnnimationState();

        for (ActorObject act : objects.keySet()) {
            data[idx] = objects.get(act);
            idx++;
            add(shortToByte((short) act.getPosition().x), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte((short) act.getPosition().y), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte((short) act.getDirection().angle()), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte(act.getuID()), data, idx);
            idx += INT_BYTE_SIZE;
            data[idx++] = act.getAnnimationState();
        }
        return data;
    }

    @Override
    public void parse() {
        objects.clear();
        valid = false;

        if (messageData.length % SIZE_PER_EL != 0) {
            System.out.println("StatusMessage#parse: Specting length % 10 = 0, found " + messageData.length % SIZE_PER_EL);
            return;
        }

        for (int loop = 0; loop < messageData.length; loop += SIZE_PER_EL) {

            /**
             * byte structure:<br>
             * 0 = type;<br>
             * 1 = first posX;<br>
             * 2 = second posX;<br>
             * 3 = first posY;<br>
             * 4 = second posY;<br>
             * 5 = first angle;<br>
             * 6 = second angle;<br>
             * 7 = first uid;<br>
             * 8 = second uid;<br>
             */
            int idx = loop;

            byte type = messageData[idx++];

            byte[] posX = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] posY = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] angle = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] uidb = new byte[]{messageData[idx++], messageData[idx++]};

            final ActorObject actor = getActor(type, bytesToShort(posX), bytesToShort(posY), bytesToShort(angle), bytesToShort(uidb), messageData[idx++]);

            if (actor != null) {
                if (loop == 0) { // is first element? then it is the currentplayer
                    currentPlayer = actor;
                } else {
                    objects.put(actor, type);
                }
            }
        }

        valid = true;

    }

    public StatusMessage setCurrentPlayer(ActorObject player) {
        currentPlayer = player;
        return this;
    }

    public ActorObject getCurrentPlayer() {
        return currentPlayer;
    }

    public Collection<ActorObject> getObjects() {
        return objects.keySet();
    }

    @Override
    public String toString() {
        return "StatusMessage{" + "valid=" + valid + ", currentPlayer=" + currentPlayer + ", objects=" + objects + '}';
    }

}
