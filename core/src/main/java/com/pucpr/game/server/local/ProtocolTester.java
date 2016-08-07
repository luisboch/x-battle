/**
 * ProtocolTester.class
 */
package com.pucpr.game.server.local;

import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.server.messages.CommandMessage;
import com.pucpr.game.server.messages.ConnectMessage;
import com.pucpr.game.server.messages.FullMessage;
import com.pucpr.game.server.messages.StatusMessage;
import com.pucpr.game.states.game.Planet;
import com.pucpr.game.states.game.Player;
import com.pucpr.game.states.game.Player2;
import com.pucpr.game.states.game.engine.ActorObject;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 7, 2016
 */
public class ProtocolTester {

    public static void test() {
        System.out.println("PROTOCOL TESTER: ");
        FullMessage fullMesg = new FullMessage();
        byte type = 3;
        byte[] connectBytes = fullMesg.build(new ConnectMessage(type));

        final ConnectMessage connectMessage = (ConnectMessage) fullMesg.parse(connectBytes);
        if (connectMessage == null || !connectMessage.isValid() || connectMessage.getType() != type) {
            throw new IllegalStateException("Connect test failed!");
        } else {
            System.out.println("Connect test:\t\tsuccess!");
        }

        short mouseX = 6123;
        short mouseY = 5987;

        final CommandMessage command = new CommandMessage();
        command.setDOWN(true);
        command.setLEFT(true);
        command.setACTION1(true);
        command.setACTION2(true);
        command.setMouseX(mouseX);
        command.setMouseY(mouseY);

        byte[] commandBytes = fullMesg.build(command);

        CommandMessage commandMessage = (CommandMessage) fullMesg.parse(commandBytes);
        if (commandMessage == null || !commandMessage.isValid()
                || commandMessage.isUP()
                || commandMessage.isRIGHT()
                || !commandMessage.isDOWN()
                || !commandMessage.isLEFT()
                || commandMessage.isFORCE()
                || !commandMessage.isACTION1()
                || commandMessage.isACTION2()
                || commandMessage.isACTION3()
                || commandMessage.getMouseX() != mouseX
                || commandMessage.getMouseY() != mouseY) {
            throw new IllegalStateException("Command test failed!");
        } else {
            System.out.println("Command test:\t\tsuccess!");
        }

        final StatusMessage status = new StatusMessage(type, commandBytes);

        Player p = new Player();
        p.setPosition(new Vector2(200, 100));
        p.setDirection(new Vector2().rotate(90));

        Player2 p2 = new Player2();
        p2.setPosition(new Vector2(100, 100));
        p2.setDirection(new Vector2().rotate(180));

        Planet pl = new Planet();
        pl.setPosition(new Vector2(-200, 100));
        pl.setDirection(new Vector2());

        status.add(p).add(p2).add(pl);

        byte[] statusBytes = fullMesg.build(status);
        final StatusMessage statusMessage = (StatusMessage) fullMesg.parse(statusBytes);

        if (statusMessage == null || !statusMessage.isValid() || statusMessage.getObjects().size() != 3) {
            throw new IllegalStateException("Status test failed!");
        } else {
            int plInst = 0;
            for (ActorObject obj : status.getObjects()) {
                if (obj instanceof Player) {
                    compare(obj, p);
                } else if (obj instanceof Player2) {
                    compare(obj, p2);
                } else if (obj instanceof Planet) {
                    compare(obj, pl);
                }

            }

            System.out.println("Status test:\t\tsuccess!");
        }
    }

    private static boolean compare(ActorObject ob1, ActorObject ob2) {
        if (!ob1.getPosition().equals(ob2.getPosition())
                || (int) ob1.getDirection().angle() != (int) ob2.getDirection().angle()) {
            throw new IllegalStateException("[" + ob1.getClass().getSimpleName() + "]ob1 not equals of [" + ob2.getClass().getSimpleName() + "]ob2");
        }
        return true;

    }
}
