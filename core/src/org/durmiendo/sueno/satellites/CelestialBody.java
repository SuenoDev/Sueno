package org.durmiendo.sueno.satellites;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.ui.ImageButton;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;

public abstract class CelestialBody {
    public Planet planet;
    public float orbitRadius;
    public float distance;
    public float spacing;
    public float speed;

    public Vec3 position;
    public Vec3 center;
    public float health;
    public Color c;
    public ImageButton button;

    public CelestialBody(float r, float spacing, float distance, Planet planet) {
        this.spacing = spacing;
        this.orbitRadius = r;
        this.planet = planet;
        this.distance = distance;



        center = planet.position;
        position = new Vec3();
        button = new ImageButton();
        button.setSize(25);
    }


    public Vec3 newPos(Vec3 r, float spacing_, float distance_) {
        r.x = orbitRadius * Mathf.cos(Mathf.degRad * spacing_ ) * Mathf.cos(Mathf.degRad * distance_ ) + center.x;
        r.y = orbitRadius * Mathf.cos(Mathf.degRad * spacing_ ) * Mathf.sin(Mathf.degRad * distance_ ) + center.y;
        r.z = orbitRadius * Mathf.sin(Mathf.degRad * spacing_ ) + center.z;

        return r;
    }

    public void draw() {
        Vec3 e = Vars.renderer.planets.cam.project(position);
        Drawf.square(e.x, e.y, 15, 0, c);
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

    public void collision(CelestialBody o) {
        if (speed < o.speed) {
            damage(o.speed - speed + 30);
        } else {
            damage(speed - o.speed + 30);
        }
    }

    public void collision(CelestialBody o, int s, int a) {
        if (speed < o.speed) {
            damage((o.speed - speed + 30) * s * a / SVars.def);
        } else {
            damage((speed - o.speed + 30) * s * a / SVars.def);
        }
    }

    public void destroy() {
        SVars.celestialBodyController.removeCB(this);
    };
}
