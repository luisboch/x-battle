/**
 * ParticleEmitter.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class ParticleEmitter extends ActorObject {

    private com.badlogic.gdx.graphics.g2d.ParticleEmitter emitter;

    public ParticleEmitter() {
        super(0f, 0f, 0f, 0f);
        emitter = new com.badlogic.gdx.graphics.g2d.ParticleEmitter();
        emitter.setContinuous(true);
    }

    @Override
    protected TextureRegion getTexture() {
        return null;
    }

    @Override
    protected void tick() {
        emitter.addParticle();
    }

    @Override
    public void draw(SpriteBatch render, Matrix3 world) {

        tick();

        if (isAlive()) {

            final float angle = this.getAngle();

            final TextureRegion texture = this.getTexture();

            world = new Matrix3(world).mul(new Matrix3().setToTranslation(this.getPosition()));
            world.mul(new Matrix3().rotate(angle));
            world.mul(new Matrix3().setToTranslation(getPivot().x, getPivot().y));

            if (texture != null) {
                lastWorldPos = new Vector2();

                world.getTranslation(lastWorldPos);

                emitter.setPosition(lastWorldPos.x, lastWorldPos.y);
                
                if (emitter != null) {
                    emitter.draw(render, Gdx.graphics.getDeltaTime());
                }

                for (ActorObject c : getListActorObject()) {
                    c.draw(render, new Matrix3(world).mul(new Matrix3().rotate(-angle)));
                }
            }
        }

    }

}
