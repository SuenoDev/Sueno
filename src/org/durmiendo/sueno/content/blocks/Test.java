package org.durmiendo.sueno.content.blocks;

import arc.graphics.Color;
import arc.util.Time;
import mindustry.entities.TargetPriority;
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
                () -> ((entity.temperature - min) / (max - min))
        ));
    }

    public class SuenoBlockBuild extends SBuilding {
        @Override
        public void update() {

            if (isHeated) {
                temperature += SVars.freezingPower / 60f * Time.delta;
            } else {
                temperature -= SVars.freezingPower / 60f * Time.delta;
            }

            temp();

            isHeated = false;
        }

        public void temp() {
            //int damage = 0;
            if (temperature <= min) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }

            if (temperature >= max) {
                health -= maxHealth * SVars.frostDamage / 60f * Time.delta;
                if (health < 0) kill();
            }
            //return damage;
        }
    }
}
