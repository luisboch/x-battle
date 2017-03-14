/**
 * Planet.class
 */

package com.pucpr.game.states.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pucpr.game.resources.ResourceLoader;
import com.pucpr.game.states.game.engine.ActorObject;
/**
 *
 * @author Luis Boch 
 * @email luis.c.boch@gmail.com
 * @since Aug 3, 2016
 */
public class Planet extends ActorObject{
    private Animation animation;

    public Planet() {
        super(50, 100000, 100, 100);
    }

    
    @Override
    protected TextureRegion getTexture() {
        return animation.getKeyFrame(0);
    }

    @Override
    protected void setupGraphics() {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/basic.png");
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation = new Animation(01f, split[0][0]);
    }


}
