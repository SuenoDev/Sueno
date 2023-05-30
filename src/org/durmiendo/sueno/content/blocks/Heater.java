package org.durmiendo.sueno.content.blocks;

import arc.func.Intc2;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.util.Log;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.core.World;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.maps.Map;
import mindustry.type.Category;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.basic.SuenoBlock;
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

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;

        @Override
        public void update() {
            if ( temperature <= attributes.get(SAttributes.temperatureMin)) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }

            tile.circle(300, (b) -> {
                if (b.build instanceof SuenoBlockBuild) {
                    SuenoBlockBuild sb = (SuenoBlockBuild) b.build;
                    sb.temperature += SVars.freezingPower / 60f * Time.delta;
                }
            });
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
