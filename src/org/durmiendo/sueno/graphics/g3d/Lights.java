package org.durmiendo.sueno.graphics.g3d;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import mindustry.gen.Building;
import mindustry.gen.Unit;

public class Lights {
    public static ObjectSet<Light> lights = new ObjectSet<>();
    public static ObjectMap<Light, Building> buildings = new ObjectMap<>();
    public static ObjectMap<Light, Unit> units = new ObjectMap<>();


    public static void each(Cons<Light> lc) {
        for (Light l : lights) {
            Building b = buildings.get(l);
            if (b != null) {
                l.pos.set(b.x + Mathf.cos(b.rotdeg()-180f) * l.relPos.x, l.pos.y, b.y + Mathf.sin(b.rotdeg()-180f) * l.relPos.y);
                if (!b.isAdded()) {
                    lights.remove(l);
                    continue;
                }
            }

            Unit u = units.get(l);
            if (u != null) {
                if (!u.isAdded()) {
                    lights.remove(l);
                    continue;
                }
                l.pos.set(u.x + Mathf.cos(u.rotation-180f) * l.relPos.x, l.pos.y, u.y + Mathf.sin(u.rotation-180f) * l.relPos.y);
            }

            lc.get(l);
        }
    }

    public static class Light {
        public Vec2 relPos = new Vec2(0, 0);
        public Vec3 pos = new Vec3(0, 0, 0);
        public float scale = 1f;
        public Color color = Color.white;
    }
}