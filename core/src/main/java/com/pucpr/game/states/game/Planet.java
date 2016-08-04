/**
 * Planet.class
 */

package com.pucpr.game.states.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.states.game.engine.ActorObject;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luis Boch 
 * @email luis.c.boch@gmail.com
 * @since Aug 3, 2016
 */
public class Planet extends ActorObject{
    private Animation animation;

    public Planet() {
        super(100, 1000000, 100, 100);
        loadAnnimation();
    }

    
    @Override
    public TextureRegion getTexture() {
        return animation.getKeyFrame(0);
    }
    
    
    private void loadAnnimation() {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/basic.png");
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation = new Animation(01f, split[0][0]);
    }


}
