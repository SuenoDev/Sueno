package org.durmiendo.sueno.basic;


import arc.graphics.Color;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.ui.Bar;
import mindustry.world.Block;

import org.durmiendo.sueno.SVars;
import org.durmiendo.sueno.content.SAttributes;
import org.durmiendo.sueno.content.blocks.Heater;


public class SuenoBlock extends Block {
    public Category category = Category.defense;
    public SuenoBlock(String name) {
        super(name);
        update = true;
        size = 1;
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
                () -> ((entity.temperature - attributes.get(SAttributes.temperatureMin)) / (0 - attributes.get(SAttributes.temperatureMin)))
        ));
    }

    public class SuenoBlockBuild extends Building {

        public float temperature = -100;


        @Override
        public void update() {
            if (temperature <= attributes.get(SAttributes.temperatureMin)) {
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