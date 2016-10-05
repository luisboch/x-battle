/**
 * Projectile.class
 */
package com.pucpr.game.states.game.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pucpr.game.resources.ResourceLoader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luis
 */
public abstract class Projectile extends ActorObject {

    private static Map<Class<? extends Projectile>, Float> reloadTimeConfig = new HashMap<Class<? extends Projectile>, Float>();

    static {
        reloadTimeConfig.put(Shot.class, 500f);
        reloadTimeConfig.put(SimpleShot.class, 300f);
    }
    
    private Animation animation;
    protected float explosionRadius = 100;
    protected float explosionForce = 100000; // The force that this Projectile cause;
    protected int lifeTime = 7000; // The life of this projectile (max will be 30 secs)
    protected boolean canExplode = false;
    protected float initialVelocity = 200f;
    protected ActorObject from;

    public Projectile(final String image, float radius, float mass, float maxVel,
            float maxForce, Vector2 size, float explosionRadius,
            float explosionForce, int lifeTime) {
        super(radius, mass, maxVel, maxForce, size);
        loadAnnimation(image);
        this.explosionRadius = explosionRadius;
        this.explosionForce = explosionForce;
        this.lifeTime = lifeTime > 30000 ? 30000 : lifeTime;
    }

    @Override
    protected TextureRegion getTexture() {
        return animation.getKeyFrame(0);
    }

    private void loadAnnimation(String image) {
        ResourceLoader loader = ResourceLoader.getInstance();
        Texture texture = loader.getTexture("data/image/" + image);
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth(), texture.getHeight());

        animation = new Animation(01f, split[0][0]);
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public boolean canExplodeNow(boolean timedout) {
        return canExplode;
    }

    public Explosion createExplosion() {
        return new Explosion(this.getPosition(), explosionForce, explosionRadius);
    }

    public float getInitialVelocity() {
        return initialVelocity;
    }

    public void setFrom(ActorObject from) {
        this.from = from;
    }

    @Override
    public void contact(ActorObject e) {

        super.contact(e);
        if (e != null && from != null && !from.equals(e) && (e.getParent() == null || e.getParent() != from) && !(e instanceof Explosion)) {
            canExplode = true;
        }
    }

    public static Map<Class<? extends Projectile>, Float> getReloadTimeConfig() {
        return reloadTimeConfig;
    }

    public void setInitialVelocity(float initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

}
