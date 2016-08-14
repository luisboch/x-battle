/**
 * Message.class
 */
package com.pucpr.game.server.messages;

import com.pucpr.game.server.ActorControl;
import java.util.Arrays;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 6, 2016
 */
public class CommandMessage extends Message {

    private static final int MESSAGE_LENGHT = 6;
    /**
     * UP
     */
    private byte command0 = 0;

    /**
     * RIGHT
     */
    private byte command1 = 0;

    /**
     * DOWN
     */
    private byte command2 = 0;

    /**
     * LEFT
     */
    private byte command3 = 0;

    /**
     * FORCE
     */
    private byte command4 = 0;

    /**
     * ACTION 1
     */
    private byte command5 = 0;

    /**
     * ACTION 2
     */
    private byte command6 = 0;

    /**
     * ACTION 3
     */
    private short command7 = 0;

    /**
     * MOUSE POINT X
     */
    private short mouseX = 0;

    /**
     * MOUSE POINT Y
     */
    private short mouseY = 0;

    boolean valid = false;

    public CommandMessage() {
        super(CURRENT_PROTOCOL, new byte[0]);
    }

    public CommandMessage(ActorControl ctrl) {
        this();
        this.command0 = ctrl.isUp() ? (byte) 1 : (byte) 0;
        this.command1 = ctrl.isRight() ? (byte) 1 : (byte) 0;
        this.command2 = ctrl.isDown() ? (byte) 1 : (byte) 0;
        this.command3 = ctrl.isLeft() ? (byte) 1 : (byte) 0;
        this.command4 = ctrl.isForce() ? (byte) 1 : (byte) 0;
        this.command5 = ctrl.isAction1() ? (byte) 1 : (byte) 0;
        this.command6 = ctrl.isAction2() ? (byte) 1 : (byte) 0;
        this.command7 = ctrl.isAction3() ? (byte) 1 : (byte) 0;
        this.mouseX = ctrl.getMouseX();
        this.mouseY = ctrl.getMouseY();
    }

    public CommandMessage(byte protocol, byte[] messageData) {
        super(protocol, messageData);
    }

    @Override
    public void parse() {
        if (messageData == null || messageData.length < MESSAGE_LENGHT) {
            System.out.println("CommandMessage#parse: received invalid message: " + Arrays.toString(messageData));
            valid = false;
        } else {

            if (protocol == 1) {
                parseV1(messageData);
            }

            valid = true;
        }
    }

    private void parseV1(byte[] message) {

        byte cmdb1 = message[0];
        byte cmdb2 = message[1];

        // UP 
        command0 = getBit(5, cmdb1);

        // RIGHT
        command1 = getBit(4, cmdb1);

        // DOWN
        command2 = getBit(3, cmdb1);

        // LEFT
        command3 = getBit(2, cmdb1);

        // FORCE
        command4 = getBit(1, cmdb1);

        // ACTION 1 
        command5 = getBit(0, cmdb1);

        // ACTION 2 
        command6 = getBit(1, cmdb2);
        
        // ACTION 3
        command7 = getBit(0, cmdb2);

        // MOUSE X
        mouseX = (short) bytesToShort(message[2], message[3]);

        // MOUSE Y
        mouseY = (short) bytesToShort(message[4], message[5]);

    }

    /**
     *
     * @return
     */
    @Override
    public byte[] build() {
        byte[] result = new byte[MESSAGE_LENGHT];
        StringBuilder builder = new StringBuilder();

        int idx = 0;

        builder.append(command0);
        builder.append(command1);
        builder.append(command2);
        builder.append(command3);
        builder.append(command4);
        builder.append(command5);

        result[idx++] = buildByte(builder.toString());
        builder = new StringBuilder();
        builder.append(command6);
        builder.append(command7);
        result[idx++] = buildByte(builder.toString());

        byte[] mouseXb = shortToByte(mouseX);
        byte[] mouseYb = shortToByte(mouseY);

        add(mouseXb, result, idx);
        idx += mouseXb.length;

        add(mouseYb, result, idx);
        idx += mouseYb.length;

        return result;

    }

    public boolean isUP() {
        return command0 == 1;
    }

    public boolean isRIGHT() {
        return command1 == 1;
    }

    public boolean isDOWN() {
        return command2 == 1;
    }

    public boolean isLEFT() {
        return command3 == 1;
    }

    public boolean isFORCE() {
        return command4 == 1;
    }

    public boolean isACTION1() {
        return command5 == 1;
    }

    public boolean isACTION2() {
        return command6 == 1;
    }

    public boolean isACTION3() {
        return command7 == 1;
    }

    public short getMouseX() {
        return mouseX;
    }

    public short getMouseY() {
        return mouseY;
    }

    public void setUP(boolean val) {
        command0 = val ? (byte) 1 : 0;
    }

    public void setRIGHT(boolean val) {
        command1 = val ? (byte) 1 : 0;
    }

    public void setDOWN(boolean val) {
        command2 = val ? (byte) 1 : 0;
    }

    public void setLEFT(boolean val) {
        command3 = val ? (byte) 1 : 0;
    }

    public void setFORCE(boolean val) {
        command4 = val ? (byte) 1 : 0;
    }

    public void setACTION1(boolean val) {
        command5 = val ? (byte) 1 : 0;
    }

    public void setACTION2(boolean val) {
        command6 = val ? (byte) 1 : 0;
    }

    public void setACTION3(boolean val) {
        command7 = val ? (byte) 1 : 0;
    }

    public void setMouseX(short mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(short mouseY) {
        this.mouseY = mouseY;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommandMessage other = (CommandMessage) obj;
        if (this.command0 != other.command0) {
            return false;
        }
        if (this.command1 != other.command1) {
            return false;
        }
        if (this.command2 != other.command2) {
            return false;
        }
        if (this.command3 != other.command3) {
            return false;
        }
        if (this.command4 != other.command4) {
            return false;
        }
        if (this.command5 != other.command5) {
            return false;
        }
        if (this.command6 != other.command6) {
            return false;
        }
        if (this.command7 != other.command7) {
            return false;
        }
        if (this.mouseX != other.mouseX) {
            return false;
        }
        if (this.mouseY != other.mouseY) {
            return false;
        }
        if (this.valid != other.valid) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "CommandMessage{" + "command0=" + command0 + ", command1=" + command1
                + ", command2=" + command2 + ", command3=" + command3 + ", command4=" + command4
                + ", command5=" + command5 + ", command6=" + command6 + ", command7=" + command7
                + ", mouseX=" + mouseX + ", mouseY=" + mouseY + ", valid=" + valid + '}';
    }

}
