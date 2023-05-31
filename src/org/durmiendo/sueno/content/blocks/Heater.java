package org.durmiendo.sueno.content.blocks;

import arc.func.Intc2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.core.World;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.gen.PayloadUnit;
import mindustry.graphics.Drawf;
import mindustry.maps.Map;
import mindustry.type.Category;
import mindustry.ui.Bar;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.basic.SBuilding;
import org.durmiendo.sueno.basic.SuenoBlock;
import org.durmiendo.sueno.basic.SuenoStat;
import org.durmiendo.sueno.content.SAttributes;

import static mindustry.Vars.indexer;
import static mindustry.Vars.world;


public class Heater extends SuenoBlock {
    public Heater(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        priority = TargetPriority.core;
        category = Category.defense;
        //it's a wall of course it's supported everywhere
        //envEnabled = Env.any;
    }
    @Override
    public void setStats() {
        super.setStats();
        this.stats.add(SuenoStat.temperature, "minimum @", this.attributes.get(SAttributes.temperatureMin));
    }


    @Override
    public void setBars() {
        super.setBars();

        addBar("temperatureBar", (SuenoBlockBuild entity) -> new Bar(
                () -> "Temperature " + String.format("%.1f",entity.temperature),
                () -> Color.cyan,
                () -> ((entity.temperature - min) / (30 - min))
        ));
    }

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;
        public int rad = 60;

        @Override
        public void update() {
            if (temperature <= min) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }

            tile.circle((int) (rad / 7.5f), (b) -> {
                if(b.build instanceof SBuilding){
                    SBuilding sb = (SBuilding) b.build;
                    sb.temperature += SVars.freezingPower / 60f * Time.delta;
                }
            });
        }

        @Override
        public void draw() {
            super.draw();
            Drawf.circles(x, y, rad, new Color(0xffa50027));
        }

        @Override
        public void readBase(Reads read) {
            this.temperature = read.f();
            super.readBase(read);
        }

        @Override
        public void writeBase(Writes write) {
            write.f(temperature);
            super.writeBase(write);
        }
    }
}
