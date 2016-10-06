/**
 * ParticleEmitter.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;

public class ParticleEmitter extends ActorObject {

    private com.badlogic.gdx.graphics.g2d.ParticleEffect effect;
    private boolean running = false;

    public ParticleEmitter(final String particleConfigFile) {
        super(0.0001f, 0.0001f, 0.0001f, 0.0001f);
        setDirection(new Vector2(0.0001f, 0.0001f));
        setPosition(new Vector2(0.0001f, 0.0001f));

        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/particles/" + particleConfigFile), Gdx.files.internal("data/particles"));

    }

    @Override
    public void draw(SpriteBatch render, Matrix3 world) {

        tick();

        if (isAlive()) {

            final float angle = this.getAngle();
            final float parentAngle = getParent().getAngle();

            final TextureRegion texture = this.getTexture();

            world = new Matrix3(world).mul(new Matrix3().rotate(parentAngle));
            world = new Matrix3(world).mul(new Matrix3().setToTranslation(this.getPosition()));
            world.mul(new Matrix3().rotate(angle));
            world.mul(new Matrix3().setToTranslation(getPivot().x, getPivot().y));

            lastWorldPos = new Vector2();

            world.getTranslation(lastWorldPos);

            effect.getEmitters().first().setPosition(lastWorldPos.x, lastWorldPos.y);

            if (effect != null) {
                effect.getEmitters().first().getAngle().setHigh(parentAngle - 180);
                effect.getEmitters().first().getAngle().setLow(parentAngle - 180);
                effect.draw(render);
            }

            for (ActorObject c : getListActorObject()) {
                c.draw(render, new Matrix3(world).mul(new Matrix3().rotate(-angle)));
            }
        }

    }

    @Override
    protected TextureRegion getTexture() {
        return null;
    }

    @Override
    protected void tick() {
        effect.update(Gdx.graphics.getDeltaTime());
    }

    public void start() {
        effect.start();
        running = true;
    }

    public void stop() {
        effect.allowCompletion();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

}
