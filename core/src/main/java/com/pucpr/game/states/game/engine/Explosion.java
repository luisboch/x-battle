/**
 * Explosion.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pucpr.game.resources.ResourceLoader;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 10, 2016
 */
public class Explosion extends ActorObject {

    private Animation animation;
    private final float force;
    private float currentForce;
    private long creationTime = System.currentTimeMillis();
    private boolean alive = true;
    private float timeToAlive = 2f; //seconds
    private float stateTime;

    public Explosion() {
        this(new Vector2(2, 2), 0f, 0f);
    }

    public Explosion(Vector2 position, float force, float radius) {
        super(radius, 1f, 150, 150);
        this.setPosition(position);
        this.force = force;
        this.currentForce = force;
        loadAnnimation();
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void tick() {
        final Long aliveTime = System.currentTimeMillis() - creationTime;
        float percent = 1 - (aliveTime.floatValue() / 1000f) / timeToAlive;
        currentForce = force * percent;

        alive = currentForce > 1;
    }

    private void loadAnnimation() {

        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/explosions/explosion1.png");
        TextureRegion[][] split = TextureRegion.split(texture, 100, 100);

        Array<TextureRegion> textures = new Array<TextureRegion>();

        textures.add(split[0][0]); // 1
        textures.add(split[0][1]); // 2
        textures.add(split[0][2]); // 3
        textures.add(split[0][3]); // 4
        textures.add(split[0][4]); // 5
        textures.add(split[0][5]); // 6
        textures.add(split[0][6]); // 7
        textures.add(split[0][7]); // 8
        textures.add(split[0][8]); // 9
        textures.add(split[0][9]); // 10

        textures.add(split[1][0]); // 11
        textures.add(split[1][1]); // 12
        textures.add(split[1][2]); // 13
        textures.add(split[1][3]); // 14
        textures.add(split[1][4]); // 15
        textures.add(split[1][5]); // 16
        textures.add(split[1][6]); // 17
        textures.add(split[1][7]); // 18
        textures.add(split[1][8]); // 19
        textures.add(split[1][9]); // 20

        textures.add(split[2][0]); // 21
        textures.add(split[2][1]); // 22
        textures.add(split[2][2]); // 23
        textures.add(split[2][3]); // 24
        textures.add(split[2][4]); // 25
        textures.add(split[2][5]); // 26
        textures.add(split[2][6]); // 27
        textures.add(split[2][7]); // 28
        textures.add(split[2][8]); // 29
        textures.add(split[2][9]); // 30

        textures.add(split[3][0]); // 31
        textures.add(split[3][1]); // 32
        textures.add(split[3][2]); // 33
        textures.add(split[3][3]); // 34
        textures.add(split[3][4]); // 35
        textures.add(split[3][5]); // 36
        textures.add(split[3][6]); // 37

        final float frame = timeToAlive / textures.size;
        animation = new Animation(frame, textures);

        stateTime = 0;
    }

    @Override
    protected TextureRegion getTexture() {
        stateTime += Gdx.graphics.getDeltaTime();
        return animation.getKeyFrame(stateTime);
    }

    public float getCurrentForce() {
        return currentForce;
    }

    public void setCurrentForce(float currentForce) {
        this.currentForce = currentForce;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public byte getAnnimationState() {
        return ((Float)(stateTime * 10)).byteValue();
    }

    @Override
    public void setAnnimationState(byte val) {
       stateTime = ((Float)(new Float((float)val) / 10f));
    }

}
