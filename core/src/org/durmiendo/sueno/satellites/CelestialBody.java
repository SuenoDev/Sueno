package org.durmiendo.sueno.satellites;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mat;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.ui.ImageButton;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import mindustry.type.Planet;
import org.durmiendo.sueno.core.SVars;

public abstract class CelestialBody {
    public Planet planet;
    public float orbitRadius;
    public float yaw;
    public float pitch;
    public float speed;
    public Mat rotation;

    public Vec3 position;
    public Vec3 center;
    public float health;
    public ImageButton button;
    public TextureRegion r;

    public CelestialBody(float r, float pitch, float yaw, Planet planet) {
        this.pitch = pitch;
        this.orbitRadius = r;
        this.planet = planet;
        this.yaw = yaw;
        this.r = Core.atlas.find("sueno-satellite");



        center = planet.position;
        position = new Vec3();
        button = new ImageButton();
        button.setSize(25);
        this.r.scale=2f;
        rotation = new Mat(){{

        }};
    }

    public Mat mathRotation() {
        Mat o = new Mat(new float[]{
                Mathf.cos(pitch * Mathf.degRad), 0, -Mathf.sin(pitch * Mathf.degRad),
                0, 1, 0,
                Mathf.sin(pitch * Mathf.degRad), 0, Mathf.cos(pitch * Mathf.degRad)
        });
        Mat t = new Mat(new float[]{
                Mathf.cos(yaw * Mathf.degRad), Mathf.sin(yaw * Mathf.degRad), 0,
                Mathf.sin(yaw * Mathf.degRad), Mathf.cos(yaw * Mathf.degRad), 0,
                0, 0, 1
        });

        return t.mul(o);
    }


    public Vec3 newPos(Vec3 r, float pitch, float yaw) {
        r.x = orbitRadius * Mathf.cos(Mathf.degRad * pitch ) * Mathf.cos(Mathf.degRad * yaw ) + center.x;
        r.y = orbitRadius * Mathf.cos(Mathf.degRad * pitch ) * Mathf.sin(Mathf.degRad * yaw ) + center.y;
        r.z = orbitRadius * Mathf.sin(Mathf.degRad * pitch ) + center.z;

        return r;
    }

    public boolean speedType=true;

    public void draw() {
        if (speedType) pitch += speed / 200 * Time.delta;
        else yaw += speed / 200 * Time.delta;
        newPos(position, pitch, yaw);
        Vec3 e = Vars.renderer.planets.cam.project(position);
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
