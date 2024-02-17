package org.durmiendo.sueno.satellites;

import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.Log;
import arc.util.Time;
import mindustry.Vars;
import mindustry.type.Planet;
import mindustry.ui.Fonts;

public class CharSatellite extends CelestialBody{
    public char name;
    public CharSatellite(float r, float pitch, float yaw, Planet planet, char name) {
        super(r, pitch, yaw, planet);

        this.name = name;
    }

    @Override
    public void draw() {
        angle += speed * Time.delta;
        angle %= 2 * Mathf.pi;

        position = newPos();
        Vec3 e = Vars.renderer.planets.cam.project(new Vec3(position));
        Fonts.def.draw(String.valueOf(name), e.x, e.y);
    }
}
