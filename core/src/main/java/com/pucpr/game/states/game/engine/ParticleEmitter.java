/**
 * ParticleEmitter.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.resources.ResourceLoader;

public class ParticleEmitter extends ActorObject {

    private com.badlogic.gdx.graphics.g2d.ParticleEffect effect;
    private boolean running = false;
    private boolean followParentAngle = false;

    public ParticleEmitter(final String particleConfigFile) {
        super(0.0001f, 0.0001f, 0.0001f, 0.0001f);
        init(particleConfigFile);
    }

    public ParticleEmitter(final String particleConfigFile, float radius, float mass, float width, float height) {
        super(radius, mass, width, height);
        init(particleConfigFile);

    }

    public ParticleEmitter(final String particleConfigFile, float radius, float mass, float maxVel, float width, float height) {
        super(radius, mass, maxVel, width, height);
        init(particleConfigFile);

    }

    private void init(final String particleConfigFile) {
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("data/particles/" + particleConfigFile), Gdx.files.internal("data/particles"));
        setDirection(new Vector2(0.0001f, 0.0001f));
        setPosition(new Vector2(0.0001f, 0.0001f));
    }

    @Override
    public void draw(SpriteBatch render, Matrix3 world, OrthographicCamera camera) {

        tick();

        if (isAlive()) {

            final float angle = this.getAngle();
            final float parentAngle = getParent().getAngle();

            final TextureRegion texture = this.getTexture();
            if (followParentAngle) {
                world = new Matrix3(world).mul(new Matrix3().rotate(parentAngle));
            }

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
                c.draw(render, new Matrix3(world).mul(new Matrix3().rotate(-angle)), camera);
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

    public boolean isFollowParentAngle() {
        return followParentAngle;
    }

    public void setFollowParentAngle(boolean followParentAngle) {
        this.followParentAngle = followParentAngle;
    }

}
