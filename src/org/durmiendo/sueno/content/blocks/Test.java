package org.durmiendo.sueno.content.blocks;

import arc.graphics.Color;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.entities.TargetPriority;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.ui.Bar;
import mindustry.world.meta.BlockGroup;
import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.basic.SBuilding;
import org.durmiendo.sueno.basic.SuenoBlock;
import org.durmiendo.sueno.basic.SuenoStat;
import org.durmiendo.sueno.content.SAttributes;


public class Test extends SuenoBlock {
    public Test(String name) {
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
                () -> "Temperature " + String.format("%.1f", entity.temperature),
                () -> Color.cyan,
                () -> ((entity.temperature - min) / (30 - min))
        ));
    }

    public class SuenoBlockBuild extends SBuilding {


        @Override
        public void update() {
            if (temperature <= min) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            } else {
                temperature -= SVars.freezingPower / 60f * Time.delta;
            }
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
