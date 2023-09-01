package org.durmiendo.sueno.sattelites;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;

public abstract class ÑelestialBody {
    public Planet planet;
    public int id;
    public float orbitRadius;
    public float distance; // XY
    public float spacing; // ZX
    public float speed;

    public Vec3 position = new Vec3();
    public Vec3 center;
    public float health;

    public ÑelestialBody(int id, float r, float spacing, float distance, Planet planet) {
        this.id = id;
        this.spacing = spacing;
        this.orbitRadius = r;
        this.planet = planet;
        this.distance = distance;

        center = planet.position;
        position = new Vec3();
    }


    public Vec3 newPos(Vec3 r, float spacing_, float distance_) {
        r.x = orbitRadius * Mathf.cos(Mathf.degRad * spacing_) * Mathf.cos(Mathf.degRad * distance_) + center.x;
        r.y = orbitRadius * Mathf.cos(Mathf.degRad * spacing_) * Mathf.sin(Mathf.degRad * distance_) + center.y;
        r.z = orbitRadius * Mathf.sin(Mathf.degRad * spacing_) + center.z;

        return r;
    }

    public void update() {
        spacing += speed / 1000;
        newPos(position, spacing, distance);
    }

    public void damage(float d) {
        if ((health - d) < 0) {
            destroy();
        } else {
            health-=d;
        }
    }

    public void collision(ÑelestialBody o) {
        if (speed < o.speed) {
            damage(o.speed - speed + 30);
        } else {
            damage(speed - o.speed + 30);
        }
    };

    public void destroy() {
        SVars.celestialBodyController.removeCB(this);
    };
}
