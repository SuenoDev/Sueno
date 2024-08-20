package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.area.CArea;
import org.durmiendo.sueno.utils.SMath;

public class SirenTurret extends ContinuousTurret {

    public TextureRegion gearSmallRegion;
    public TextureRegion gearBigRegion;
    public TextureRegion shieldRegion;

    public SirenTurret(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        gearSmallRegion = Core.atlas.find("sueno-gear-small");
        gearBigRegion = Core.atlas.find("sueno-gear-big");
        shieldRegion = Core.atlas.find("sueno-sh1");
    }

    @SuppressWarnings("unused")
    public class SirenBuild extends ContinuousTurret.ContinuousTurretBuild {
        public CArea area;
        public boolean pressed = false;
        public Vec2 tmp = new Vec2(0, 50);
        public float dir = Mathf.halfPi;
        

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
            Building b = super.init(tile, team, shouldAdd, rotation);
            area = new CArea(b.x() , b.y() + 50f, 5.5f);
            SVars.input.addListener(area, this::arrow);

            arrow(b.x(), b.y() + 50f, (byte) 0);
            return b;
        }

        public void arrow(float x, float y, byte s) {
            if (s == 1) {
                pressed = true;
            } else if (s == 2) {
                pressed = false;
            }
            area.x = x();
            area.y = y();

            tmp.set(area.x, area.y);
            SMath.circlePointer(area.x, area.y, 50f, x, y, tmp);

            area.x = tmp.x;
            area.y = tmp.y;

            dir = Mathf.atan2(tmp.x - x(), tmp.y - y());
        }


        @Override
        public void remove() {
            super.remove();
            SVars.input.removeListener(area);
        }

        @Override
        public void update() {
            super.update();
        }

        @Override
        public void draw() {
            super.draw();

            Draw.rect(gearBigRegion, x, y);
            float adir;
            for (int i = 0; i < 7; i++) {
                adir = dir + (i * Mathf.pi/6f) - Mathf.pi * 0.5f;
                Draw.rect(gearSmallRegion, Mathf.cos(adir) * 38f + x(), Mathf.sin(adir) * 38f + y(), 16f, 4f, (adir * Mathf.radDeg + 90f));
                Draw.rect(shieldRegion, Mathf.cos(adir) * 44f + x(), Mathf.sin(adir) * 44f + y(), 16f, 7f, (adir * Mathf.radDeg + 90f));
            }

            if (pressed) Drawf.arrow(area.x, area.y, area.x + 2f*Mathf.cos(dir), area.y + 2f*Mathf.sin(dir), 6f, 3.5f, Color.yellow);
            else Drawf.arrow(area.x, area.y, area.x + 2f*Mathf.cos(dir), area.y + 2f*Mathf.sin(dir), 6f, 3.5f, Color.gray);
        }
    }
}
