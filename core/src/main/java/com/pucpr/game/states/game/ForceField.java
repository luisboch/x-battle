/**
 * ForceField.class
 */
package com.pucpr.game.states.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pucpr.game.states.game.engine.ParticleEmitter;

/**
 *
 * @author Felipe
 * @email felipe
 * @since Aug 10, 2016
 */
public class ForceField extends ParticleEmitter {

    private static final int INIT_HEALTH = 2000;
    float stateTime;

    public ForceField() {
        super("shield.party", 15, 1f, 200, 30, 30);
        setHealth(INIT_HEALTH);
    }

    @Override
    protected TextureRegion getTexture() {
        return null;
    }

    @Override
    protected void tick() {

        super.tick();

        setDirection(getDirection().nor().rotate(50f * Gdx.graphics.getDeltaTime()));
        if (health < INIT_HEALTH) {
            health += (50 * Gdx.graphics.getDeltaTime());
        }

        if (health < 0 && isRunning()) {
            stop();
        } else if (!isRunning()) {
            start();
        }
    }

}
