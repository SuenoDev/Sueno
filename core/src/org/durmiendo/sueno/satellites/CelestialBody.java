package org.durmiendo.sueno.satellites;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mat;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.ui.ImageButton;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.type.Planet;
import org.durmiendo.sueno.content.SPlanets;
import org.durmiendo.sueno.core.SVars;

public abstract class CelestialBody {
    public float angle;
    public Planet planet = SPlanets.hielo;
    public float orbitRadius;
    public float speed;
    public Mat rotation;

    public Vec3 position;
    public float health;
    public ImageButton button;
    public TextureRegion r;

    public CelestialBody(float r, float pitch, float yaw, Planet planet) {
        this.orbitRadius = r;
        this.planet = planet;
        this.r = Core.atlas.find("sueno-satellite");



        position = new Vec3();
        button = new ImageButton();
        button.setSize(25);
        this.r.scale=2f;
        rotation = mathRotation(pitch, yaw);
    }

    public Mat mathRotation(float pitch, float yaw) {
        Mat o = new Mat(new float[]{
                Mathf.cos(pitch), 0, -Mathf.sin(pitch),
                0, 1, 0,
                Mathf.sin(pitch), 0, Mathf.cos(pitch)
        });
        Mat t = new Mat(new float[]{
                1, 0, 0,
                0, Mathf.cos(yaw), -Mathf.sin(yaw),
                0, Mathf.sin(yaw), Mathf.cos(yaw),

        });

        return t.mul(o);
    }


    public Vec3 newPos() {
        return new Vec3(Mathf.sin(angle) * orbitRadius, Mathf.cos(angle) * orbitRadius, 0).mul(rotation).add(planet.position);
    }

    public void draw() {
        angle += speed * Time.delta;
        angle %= 2 * Mathf.pi;

        position = newPos();
        Vec3 e = Vars.renderer.planets.cam.project(new Vec3(position));
        Drawf.additive(r, Color.lightGray, e.x, e.y);
    }


    public void update() {
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

    public void destroy() {
        SVars.celestialBodyController.removeCB(this);
    };
}
