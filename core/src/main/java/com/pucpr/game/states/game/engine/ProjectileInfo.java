/**
 * ProjectileInfo.class
 */
package com.pucpr.game.states.game.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since Aug 8, 2016
 */
class ProjectileInfo {
    final Map<Class<? extends Projectile>, Long> usedTypes = new ConcurrentHashMap<Class<? extends Projectile>, Long>();
}
