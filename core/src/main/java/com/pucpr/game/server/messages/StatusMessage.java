/**
 * StatusMessage.class
 */
package com.pucpr.game.server.messages;

import com.pucpr.game.states.game.Player;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.ArrayList;
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

        /*
         * byte structure:<br>
         * 0 = type 
         * 1 = first posX 
         * 2 = second posX 
         * 3 = first posY 
         * 4 = second posY 
         * 5 = first angle 
         * 6 = second angle
         * 7 = first uid
         * 8 = second uid
         * 9 = animation state
         * 10 = current life (0-100)
         * 
         * --- Shields 
         * 11 = shield 1 first posX
         * 12 = shield 1 second posX
         * 13 = shield 1 first posY
         * 14 = shield 1 second posY
         * 15 = shield 2 first posX
         * 16 = shield 2 second posX
         * 17 = shield 2 first posY
         * 18 = shield 2 second posY
         * 19 = shield 3 first posX
         * 20 = shield 3 second posX
         * 21 = shield 3 first posY
         * 22 = shield 3 second posY
         * 23 = shield 1 life (0-100)
         * 24 = shield 2 life (0-100)
         * 25 = shield 3 life (0-100)
         */
        final List<Object> data = new ArrayList<Object>();

        int idx = 0;

        data.add(idx++, getType(currentPlayer));

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

        data.add(idx++, currentPlayer.getAnnimationState());
        data.add(idx++, (byte) (currentPlayer.getHealthPercent() * 100));

        // Shields
        List<ActorObject> shields = currentPlayer.getChildren();
        ActorObject shield1 = shields.get(0);
        ActorObject shield2 = shields.get(1);
        ActorObject shield3 = shields.get(2);

        // Shield 1
        add(shortToByte((short) shield1.getPosition().x), data, idx);
        idx += INT_BYTE_SIZE;
        add(shortToByte((short) shield1.getPosition().y), data, idx);
        idx += INT_BYTE_SIZE;

        // Shield 2
        add(shortToByte((short) shield2.getPosition().x), data, idx);
        idx += INT_BYTE_SIZE;
        add(shortToByte((short) shield2.getPosition().y), data, idx);
        idx += INT_BYTE_SIZE;

        // Shield 3
        add(shortToByte((short) shield3.getPosition().x), data, idx);
        idx += INT_BYTE_SIZE;
        add(shortToByte((short) shield3.getPosition().y), data, idx);
        idx += INT_BYTE_SIZE;

        // Shield lifes
        data.add(idx++, (byte) (shield1.getHealthPercent() * 100));
        data.add(idx++, (byte) (shield2.getHealthPercent() * 100));
        data.add(idx++, (byte) (shield3.getHealthPercent() * 100));

        for (ActorObject act : objects.keySet()) {
            data.add(idx++, objects.get(act));
            add(shortToByte((short) act.getPosition().x), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte((short) act.getPosition().y), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte((short) act.getDirection().angle()), data, idx);
            idx += INT_BYTE_SIZE;
            add(shortToByte(act.getuID()), data, idx);
            idx += INT_BYTE_SIZE;
            data.add(idx++, act.getAnnimationState());
            data.add(idx++, (byte) (act.getHealthPercent() * 100));

            if (act instanceof Player) {

                // Shields
                List<ActorObject> actShields = act.getChildren();
                ActorObject ch1 = actShields.get(0);
                ActorObject ch2 = actShields.get(1);
                ActorObject ch3 = actShields.get(2);

                // Shield 1
                add(shortToByte((short) ch1.getPosition().x), data, idx);
                idx += INT_BYTE_SIZE;
                add(shortToByte((short) ch1.getPosition().y), data, idx);
                idx += INT_BYTE_SIZE;

                // Shield 2
                add(shortToByte((short) ch2.getPosition().x), data, idx);
                idx += INT_BYTE_SIZE;
                add(shortToByte((short) ch2.getPosition().y), data, idx);
                idx += INT_BYTE_SIZE;

                // Shield 3
                add(shortToByte((short) ch3.getPosition().x), data, idx);
                idx += INT_BYTE_SIZE;
                add(shortToByte((short) ch3.getPosition().y), data, idx);
                idx += INT_BYTE_SIZE;

                // Shield lifes
                data.add(idx++, (byte) ch1.getHealthPercent());
                data.add(idx++, (byte) ch2.getHealthPercent());
                data.add(idx++, (byte) ch3.getHealthPercent());
            }
        }
        final byte[] finalData = convert(data);
        return finalData;
    }

    @Override
    public void parse() {
        objects.clear();
        valid = false;

        int idx = 0;
        boolean currentplayer = true;
        while (true) { // We will leave when complete
            if (idx >= messageData.length) {
                break;
            }

            /*
            * byte structure:<br>
            * 0 = type 
            * 1 = first posX 
            * 2 = second posX 
            * 3 = first posY 
            * 4 = second posY 
            * 5 = first angle 
            * 6 = second angle
            * 7 = first uid
            * 8 = second uid
            * 9 = animation state
            * 10 = current life (0-100)
            * 
            * --- Shields 
            * 11 = shield 1 first posX
            * 12 = shield 1 second posX
            * 13 = shield 1 first posY
            * 14 = shield 1 second posY
            * 15 = shield 2 first posX
            * 16 = shield 2 second posX
            * 17 = shield 2 first posY
            * 18 = shield 2 second posY
            * 19 = shield 3 first posX
            * 20 = shield 3 second posX
            * 21 = shield 3 first posY
            * 22 = shield 3 second posY
            * 23 = shield 1 life (0-100)
            * 24 = shield 2 life (0-100)
            * 25 = shield 3 life (0-100)
             */
            byte type = messageData[idx++];

            byte[] posX = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] posY = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] angle = new byte[]{messageData[idx++], messageData[idx++]};

            byte[] uidb = new byte[]{messageData[idx++], messageData[idx++]};

            byte animState = messageData[idx++];
            byte currentLife = messageData[idx++];
            final ActorObject actor;

            if (Player.class.isAssignableFrom(getType(type))) { // Is Player?
                byte[] sh1PosX = new byte[]{messageData[idx++], messageData[idx++]};
                byte[] sh1PosY = new byte[]{messageData[idx++], messageData[idx++]};
                byte[] sh2PosX = new byte[]{messageData[idx++], messageData[idx++]};
                byte[] sh2PosY = new byte[]{messageData[idx++], messageData[idx++]};
                byte[] sh3PosX = new byte[]{messageData[idx++], messageData[idx++]};
                byte[] sh3PosY = new byte[]{messageData[idx++], messageData[idx++]};

                byte sh1Health = messageData[idx++];
                byte sh2Health = messageData[idx++];
                byte sh3Health = messageData[idx++];
                actor = getActor(type,
                        bytesToShort(posX), bytesToShort(posY), bytesToShort(angle),
                        bytesToShort(uidb), animState, currentLife,
                        bytesToShort(sh1PosX), bytesToShort(sh1PosY),
                        bytesToShort(sh2PosX), bytesToShort(sh2PosY),
                        bytesToShort(sh3PosX), bytesToShort(sh3PosY),
                        sh1Health, sh2Health, sh3Health);

            } else {
                actor = getActor(type,
                        bytesToShort(posX), bytesToShort(posY), bytesToShort(angle), bytesToShort(uidb), animState, currentLife);
            }

            if (actor != null) {
                if (currentplayer) { // is first element? then it is the currentplayer
                    currentPlayer = actor;
                    currentplayer = false;
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

    private byte[] convert(List<Object> data) {
        
        final byte[] bytes = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            bytes[i] = (Byte) data.get(i);
        }

        return bytes;
    }

}
