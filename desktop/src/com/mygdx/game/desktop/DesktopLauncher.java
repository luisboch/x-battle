package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;

public class DesktopLauncher {

    public static void main(String[] arg) {

        if (arg.length > 0) {
            GameConfig.serverHost = arg[0]; // Connecting to another server
        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 600;
        config.title = "X-Battle";
//
//                config.addIcon("data/icon-16.png", Files.FileType.Internal);
//                config.addIcon("data/icon-32.png", Files.FileType.Internal);
//                config.addIcon("data/icon-128.png", Files.FileType.Internal);
        new LwjglApplication(new AppManager(), config);
    }
}
