package org.durmiendo.sueno.world.blocks.defense.turrets;

import arc.Core;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.entities.Units;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Bullet;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.turrets.ContinuousTurret;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.math.Maths;
import org.durmiendo.sueno.math.area.CArea;
import org.durmiendo.sueno.utils.SLog;
import org.durmiendo.sueno.utils.SMath;

public class SirenTurret extends ContinuousTurret {

    public TextureRegion gearSmallRegion;
    public TextureRegion gearBigRegion;
    public TextureRegion shieldRegion;

    protected static SirenBuild paramBuild;
    public float rad = 45f;
    protected static final Cons<Bullet> bulletConsumer = bullet -> {
        if(bullet.team != paramBuild.team && bullet.type.absorbable && bullet.within(paramBuild, paramBuild.radius())) {
            bullet.absorb();
        }
    };

    protected static final Cons<Unit> unitConsumer = unit -> {
        float overlapDst = (unit.hitSize/2f + paramBuild.radius()) - unit.dst(paramBuild);

        if(overlapDst > 0 && Maths.isAngleInSector(190f, paramBuild.dir * Mathf.radDeg + 180f, Mathf.angle(paramBuild.x - unit.x, paramBuild.y - unit.y))){
            unit.vel.x *= -1.5f;
            unit.vel.y *= -1.5f;
//            unit.move(Tmp.v1.set(unit).sub(paramBuild).setLength(overlapDst + 0.01f));

//            if(Mathf.chanceDelta(0.12f * Time.delta)){
//                Fx.circleColorSpark.at(unit.x, unit.y, paramBuild.team.color);
//            }
        }
    };


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

        public float radius() {
            return rad;
        }

        @Override
        public void drawSelect(){
            Drawf.dashCircle(x, y, range(), team.color);
        }

        protected void findTarget(){
            float range = range();

            target = Units.bestEnemy(team, x, y, range, e -> !e.dead() && unitFilter.get(e)
                    && Maths.isAngleInSector(100f, dir * Mathf.radDeg + 180f, Mathf.angle(paramBuild.x - e.x, paramBuild.y - e.y))
                    && e.dst(paramBuild) >= 40f, unitSort);


            if(target == null && canHeal()){
                target = Units.findAllyTile(team, x, y, range, b -> b.damaged() && b != this);
            }
        }


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
            SLog.unvisi(id);
        }

        @Override
        public void update() {
            SLog.visi("dir: " + dir * Mathf.radDeg, id);
            super.update();
        }

        @Override
        public void updateTile() {
            paramBuild = this;
            Groups.bullet.intersect(x - rad, y - rad, rad * 2f, rad * 2f, bulletConsumer);
            Units.nearbyEnemies(team, x, y, rad + 10f, unitConsumer);
            super.updateTile();
        }

        @Override
        public void draw() {
            super.draw();

            Draw.rect(gearBigRegion, x, y);
            float adir;
            for (int i = 0; i < 7; i++) {
                adir = dir + (i * Mathf.pi/6f) - Mathf.pi * 0.5f;
                Draw.rect(gearSmallRegion, Mathf.cos(adir) * 38f + x(), Mathf.sin(adir) * 38f + y(), 12f, 4f, (adir * Mathf.radDeg + 90f));
                Draw.rect(shieldRegion, Mathf.cos(adir) * 42f + x(), Mathf.sin(adir) * 42f + y(), 14f, 6f, (adir * Mathf.radDeg + 90f));
            }

            if (pressed) Drawf.arrow(area.x, area.y, area.x + 2f*Mathf.cos(dir), area.y + 2f*Mathf.sin(dir), 6f, 3.5f, Color.yellow);
            else Drawf.arrow(area.x, area.y, area.x + 2f*Mathf.cos(dir), area.y + 2f*Mathf.sin(dir), 6f, 3.5f, Color.gray);
        }

        @Override
        public boolean collide(Bullet other) {
            Tmp.v6.set(other.x, other.y);
            float dst = Tmp.v6.dst(x(), y());
            if (dst > 24)SLog.info("dst: " + dst);
            return super.collide(other);
        }
    }
}
